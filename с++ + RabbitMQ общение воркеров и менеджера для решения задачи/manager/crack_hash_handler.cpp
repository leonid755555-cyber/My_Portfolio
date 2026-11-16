#include <iostream>
#include <string>
#include <memory>
#include <queue>

#include "httplib.h"
#include "json.hpp"

#include "main.hpp"

void CrackHashHandler(const httplib::Request& req, httplib::Response& res) {
    try {
        auto jsonBody = nlohmann::json::parse(req.body);
        std::string hash = jsonBody["hash"];
        int maxLength = jsonBody["maxLength"];
        std::string s_requestId = hash + std::to_string(maxLength);
        int id = 0;
        if(static_cast<bool>(globals.find_one(document{} << "name" << "max_id" << finalize)))      
            if (doc["value"]) id = doc["value"].get_int32().value;

        bool needWorker = GlobalTaskStorage->AddTask(s_requestId, hash);

        if (needWorker) {
            int partCount = maxLength * partCoefficient;
            GlobalTaskStorage->SetPartCount(hash, partCount);

            for (int i = 1; i <= partCount; ++i, ++id) {
                CrackTaskRequest task{ hash, maxLength, i, partCount };
                taskQueue->Push(task, id);
            }
        }
        globals.update_one(
            document{} << "name" << "max_id" << finalize,
            document{} << "$set" << open_document << "value" << id << close_document << finalize);
        
        nlohmann::json response = {
            {"requestId", s_requestId}
        };
        res.set_content(response.dump(), "application/json");
    }
    catch (const std::exception& e) {
        std::cerr << "Failed to process request: " << e.what() << std::endl;
        res.status = 400;
        res.set_content("Invalid request body", "text/plain");
    }
 }