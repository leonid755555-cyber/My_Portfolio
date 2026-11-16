#include <iostream>
#include <map>
#include <mutex>
#include <thread>
#include <memory>
#include <queue>
#include <condition_variable>
#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/uri.hpp>

#include "httplib.h"

#include "main.hpp"


TaskDispatcher::TaskDispatcher(std::shared_ptr<TaskQueue> queue, mongocxx::database db): 
    taskQueue(queue), 
    dispatcherPartToWorkerCollection(db["part_to_worker"]) {
}

void TaskDispatcher::Start() {
    std::thread([this]() { dispatchTasks(); }).detach();
    std::thread([this]() { dispatchChecks(); }).detach();
}

void TaskDispatcher::dispatchTasks() {
    while (true) {
        auto task = taskQueue->Pop();
        auto worker = LoadBalancer->GetNextWorker();

        if (worker) {
            std::cout << "Dispatching task for hash " << task->Hash
                << " (part " << task->PartNumber << "/" << task->PartCount
                << ") to worker " << "http://" + worker->name + ":" + std::to_string(worker->port) << std::endl;
            sendTaskToWorker(worker->name, worker->port, *task);
            worker->ActiveTask = task;
        }
        else {
            std::cout << "No worker available, returning task to queue" << std::endl;
            taskQueue->Push(*task);
        }
    }
}

void TaskDispatcher::dispatchChecks() {
    while (true) {
        auto this_workers = LoadBalancer->GetWorkers(); 
        for (const auto& worker : this_workers) {
            if (!isHttpServerAlive(worker)) {
                std::cout << "Error: Worker " << worker->name << " is dead!" << std::endl;
                if (worker->ActiveTasks > 0) {//check on rabbit ack!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    taskQueue->Return_into_queue(worker);
                }
                LoadBalancer->DeleteWorker(worker);
            }
        }
        std::this_thread::sleep_for(std::chrono::seconds(3));
    }
}

bool TaskDispatcher::isHttpServerAlive(std::shared_ptr<WorkerInfo> worker) {
    httplib::Client cli("http://" + worker->name + ":" + std::to_string(worker->port));
    cli.set_connection_timeout(3);
    cli.set_read_timeout(3);

    auto res = cli.Get("/");
    if (res) {
        return true;
    } else {
        std::cerr << "Connection error: " << res.error() << std::endl;
        return false;
    }
}

void TaskDispatcher::sendTaskToWorker(const std::string& workerName, const int workerPort, const CrackTaskRequest& task) {
    std::unique_lock<std::mutex> lock(mu);
    collection.delete_one(document{} << "hash" << task.Hash << "part_number" << task.PartNumber << finalize);
    auto doc = document{}
        << "hash" << task.Hash
        << "part_number" << task.PartNumber
        << "worker_name" << workerName
        << finalize;
    
    dispatcherPartToWorkerCollection.insert_one(doc.view());

    lock.unlock();

    httplib::Client client("http://" + workerName + ":" + std::to_string(workerPort));

    auto res = client.Post("/internal/api/worker/hash/crack/task", task.toJson().dump(), "application/json");

    if (!res || res->status != 200) {
        std::cerr << "Failed to send task to worker " << workerName << std::endl;
        LoadBalancer->TaskCompleted(workerName);
        taskQueue->Push(task); 
    }
}

void TaskDispatcher::DecrementWorkerTasks(const std::string& workerName) {
    LoadBalancer->TaskCompleted(workerName);
}

std::string TaskDispatcher::GetWorkerByPart(const std::string& hash, int partNumber) {
    std::unique_lock<std::mutex> lock(mu);
    auto result = dispatcherPartToWorkerCollection.find_one(document{} << "hash" << Hash << "part_number" << partNumber << finalize);

    if(result) return result["worker_name"].get_utf8().value.to_string();
    return "";
}

void TaskDispatcher::DeleteWorkerByPart(const std::string& hash, int partNumber) {
    std::unique_lock<std::mutex> lock(mu);
    dispatcherPartToWorkerCollection.delete_one(document{} << "hash" << Hash << "part_number" << partNumber << finalize);
}