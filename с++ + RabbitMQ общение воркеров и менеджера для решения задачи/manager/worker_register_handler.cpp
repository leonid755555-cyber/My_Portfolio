#include <iostream>
#include <string>

#include "httplib.h"
#include "json.hpp"

#include "main.hpp"

void WorkerRegisterHandler(const AMQP::Message &message, uint64_t deliveryTag) {
    auto jsonBody = nlohmann::json::parse(message.message());
    std::string workerName = jsonBody["workerName"];
    int maxWorkers = jsonBody["maxWorkers"];
    int port = jsonBody["port"];

    LoadBalancer->RegisterWorker(workerName, port, maxWorkers);
}