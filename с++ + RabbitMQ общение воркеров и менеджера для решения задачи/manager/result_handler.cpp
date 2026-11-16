#include <iostream>
#include <string>
#include <memory>

#include "httplib.h"
#include "json.hpp"

#include "main.hpp"

void ResultHandler(const AMQP::Message &message, uint64_t deliveryTag) {
    auto jsonBody = nlohmann::json::parse(message.message());
    std::string hash = jsonBody["hash"];
    std::string result = jsonBody["result"];
    int partNumber = jsonBody["partNumber"];
    std::string workerName = dispatcher->GetWorkerByPart(hash, partNumber);
    if (!workerName.empty()) {
        dispatcher->DecrementWorkerTasks(workerName);
    }

    GlobalTaskStorage->AddPartResult(hash, partNumber, result);

    dispatcher->DeleteWorkerMemoryByPart(hash, partNumber);
    PopCompletedTask(hash, partNumber);
}