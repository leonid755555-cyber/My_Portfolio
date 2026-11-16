#include <map>
#include <queue>
#include <mutex>
#include <condition_variable>
#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/uri.hpp>

#include "json.hpp"
#include "httplib.h"

#ifndef CrackTask
#define CrackTask

class RabbitMQManager {
private:
    std::shared_ptr<AMQP::TcpConnection> connection;
    std::shared_ptr<AMQP::TcpChannel> channel;
    
    struct ev_loop* loop;
    AMQP::LibEvHandler* handler;
    
public:
    RabbitMQManager();
    void declareQueues();
    void run();
    ~RabbitMQManager();
};

struct CrackTaskRequest {
    std::string Hash;
    int MaxLength;
    int PartNumber;
    int PartCount;

    nlohmann::json toJson() const;
};

struct StatusResponse {
    std::string Status;
    std::vector<std::string> Data;
    
    nlohmann::json toJson() const;
};

class TaskQueue {
private:
    std::deque<CrackTaskRequest> tasks;
    std::mutex mu;
    std::condition_variable notEmpty;
    mongocxx::collection tasksCollection;
    void saveTaskToDB(const CrackTaskRequest& task, int id);

public:
    TaskQueue(mongocxx::database db);
    void Push(const CrackTaskRequest& task);
    void Return_into_queue(const CrackTaskRequest& task);
    std::shared_ptr<CrackTaskRequest> Pop();
    void PopCompletedTask(std::string Hash, int PartNumber);
};

struct WorkerInfo {
    std::string name;
    int port;
    int MaxWorkers;
    int ActiveTasks;
    CrackTaskRequest ActiveTask;
};

class TaskStorage {
private:
    mongocxx::collection dispatcherRequestToHashCollection;
    mongocxx::collection dispatcherHashToStatusCollection;
    mongocxx::collection dispatcherPartResultsCollection;
    mongocxx::collection dispatcherPartCountsCollection;
    std::mutex mu;

public:
    TaskStorage(mongocxx::database db);
    bool AddTask(std::string requestId, const std::string& hash);
    std::pair<StatusResponse, bool> GetStatus(const std::string& requestId);
    void UpdateStatus(const std::string& hash, const std::string& status, const std::vector<std::string>& result);
    void SetPartCount(const std::string& hash, int count);
    void AddPartResult(const std::string& hash, int partNumber, const std::string& result);
}; 

class TaskDispatcher {
private:
    std::shared_ptr<TaskQueue> taskQueue;
    std::mutex mu;
    mongocxx::collection dispatcherPartToWorkerCollection;

public:
    TaskDispatcher(std::shared_ptr<TaskQueue> queue, mongocxx::database db);
    void Start();
    void dispatchTasks();
    void dispatchChecks();
    bool isHttpServerAlive(std::shared_ptr<WorkerInfo> worker);
    void sendTaskToWorker(const std::string& workerName, const int workerPort, const CrackTaskRequest& task);
    void DecrementWorkerTasks(const std::string& workerName);
    std::string GetWorkerByPart(const std::string& hash, int partNumber);
    void DeleteWorkerByPart(const std::string& hash, int partNumber);
};

void CrackHashHandler(const httplib::Request& req, httplib::Response& res);
void StatusHandler(const httplib::Request& req, httplib::Response& res);
void ResultHandler(const AMQP::Message &message, uint64_t deliveryTag);
void WorkerRegisterHandler(const AMQP::Message &message, uint64_t deliveryTag);

extern std::shared_ptr<TaskQueue> taskQueue;
extern std::shared_ptr<TaskDispatcher> dispatcher;
extern std::shared_ptr<TaskStorage> GlobalTaskStorage;
extern std::shared_ptr<RoundRobin> LoadBalancer;
const int partCoefficient = 20;

void Start(std::shared_ptr<TaskQueue> taskQueue, std::shared_ptr<TaskDispatcher> taskDispatcher);
#endif