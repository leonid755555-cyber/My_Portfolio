#include <queue>
#include <mutex>
#include <condition_variable>
#include <memory>
#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/uri.hpp>

#include "main.hpp"
using bsoncxx::builder::stream::document;
using bsoncxx::builder::stream::finalize;

    
void TaskQueue::saveTaskToDB(const CrackTaskRequest& task, int id) {
    auto doc = document{}
        << "task_id" << id 
        << "hash" << task.Hash
        << "max_length" << task.MaxLength
        << "part_number" << task.PartNumber
        << "part_count" << task.PartCount
        << finalize;
    if(!tasksCollection.find_one(doc))
        tasksCollection.insert_one(doc.view());//upd + opt
}
TaskQueue::TaskQueue(mongocxx::database db)
{
    tasksCollection = db["tasks"];
    mongocxx::options::find opts;
    opts.sort(document{} << "task_id" << 1 << finalize);
    
    auto cursor = tasksCollection.find({}, opts);
    
    for (auto&& doc : cursor) {
        CrackTaskRequest task;
        
        if (doc["hash"]) task.Hash = doc["hash"].get_utf8().value.to_string();
        if (doc["max_length"]) task.MaxLength = doc["max_length"].get_int32().value;
        if (doc["part_number"]) task.PartNumber = doc["part_number"].get_int32().value;
        if (doc["part_count"]) task.PartCount = doc["part_count"].get_int32().value;
        
        tasks.push_back(task);
    }
} 

void TaskQueue::Push(const CrackTaskRequest& task, int id) {
    std::unique_lock<std::mutex> lock(mu);
    tasks.push_back(task);
    
    saveTaskToDB(task, id);
    
    notEmpty.notify_one();
}

void TaskQueue::Return_into_queue(const CrackTaskRequest& task) {
    std::unique_lock<std::mutex> lock(mu);
    tasks.push_front(task);
    notEmpty.notify_one();
}

std::shared_ptr<CrackTaskRequest> TaskQueue::Pop() {
    std::unique_lock<std::mutex> lock(mu);
    notEmpty.wait(lock, [this]() { return !tasks.empty(); });
    auto task = std::make_shared<CrackTaskRequest>(tasks.front());
    tasks.pop_front();
    return task;
}

void TaskQueue::PopCompletedTask(std::string Hash, int PartNumber) {
    try {
        if (static_cast<bool>(tasksCollection.delete_one(document{} << "hash" << Hash << "part_number" << PartNumber << finalize)))
            std::cout << "task found with ID, " << taskId << " and was deleted from MongoDB" << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "MongoDB delete error: " << e.what() << std::endl;
    }
}


