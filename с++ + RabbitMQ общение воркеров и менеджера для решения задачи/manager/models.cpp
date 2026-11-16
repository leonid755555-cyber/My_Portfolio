#include <iostream>
#include <sstream> 
#include <map>
#include <mutex>
#include <vector>
#include <string>
#include <memory>
#include <iomanip> 
#include <mongocxx/client.hpp>
#include <mongocxx/instance.hpp>
#include <mongocxx/uri.hpp>

#include "json.hpp"
#include "httplib.h"

#include "main.hpp"

nlohmann::json CrackTaskRequest::toJson() const {
    return {
        {"hash", Hash},
        {"maxLength", MaxLength},
        {"partNumber", PartNumber},
        {"partCount", PartCount}
    };
}

nlohmann::json StatusResponse::toJson() const {
    return {
        {"status", Status},
        {"data", Data}
    };
}

TaskStorage::TaskStorage(mongocxx::database db):
    RequestToHashCollection(db["request_to_hash"]),
    HashToStatusCollection(db["hash_to_status"]),
    PartResultsCollection(db["part_results"]),
    PartCountsCollection(db["part_counts"]) {
}

bool TaskStorage::AddTask(std::string requestId, const std::string& hash) {
    std::unique_lock<std::mutex> lock(mu);

    std::cout << "[TaskStorage] Adding new task. RequestID: " << requestId << ", Hash: " << hash << std::endl;

    RequestToHashCollection.update_one(
        document{} << "request_id" << requestId << finalize,
        document{} << "$set" << open_document << "hash" << hash << close_document << finalize,
        options);
    auto result = HashToStatusCollection.find_one(document{} << "hash" << hash << finalize);
    if (result["status"]) {
        auto status = result["status"].get_utf8().value.to_string();
        if (status == "READY") {
            std::cout << "[TaskStorage] Hash " << hash << " already processed, returning result" << std::endl;
            return false;
        }
        if (status == "IN_PROGRESS") {
            std::cout << "[TaskStorage] Hash " << hash << " already in progress" << std::endl;
            return false;
        }
        if (status == "FAIL") {
            std::cout << "[TaskStorage] Hash " << hash << " already processed, and it have no answer" << std::endl;
            return false;
        }
    }

    HashToStatusCollection.update_one(
        document{} << "hash" << hash << finalize,
        document{} << "$set" << open_document << "status" << "IN_PROGRESS" << "data" << "0%" << close_document << finalize,
        options);
    std::cout << "[TaskStorage] Started processing for hash " << hash << std::endl;
    return true;
}

std::pair<StatusResponse, bool> TaskStorage::GetStatus(const std::string& requestId) {
    std::unique_lock<std::mutex> lock(mu);
    auto result = RequestToHashCollection.find_one(document{} << "request_id" << requestId << finalize);
    if (!result) {
        std::cout << "[TaskStorage] Request ID " << requestId << " not found" << std::endl;
        return { StatusResponse{}, false };
    }

    if(result["hash"]) std::string hash = result["hash"].get_utf8().value.to_string();

    auto result = HashToStatusCollection.find_one(document{} << "hash" << hash << finalize);
    auto status = result["status"].get_utf8().value.to_string();

    mongocxx::cursor cursor = collection.find(
        document{} << "hash" << hash << finalize);

    std::vector<std::string> data;
    for (auto&& doc : cursor) 
        data.push_back(doc["data"].get_utf8().value.to_string());

    std::cout << "[TaskStorage] Status for request " << requestId << " (hash " << hash << "): "
        << status << " " << data.size() << " results" << std::endl;
    return { StatusResponse{ status, data }, true };
}
void TaskStorage::UpdateStatus(const std::string& hash, const std::string& status, const std::vector<std::string>& result) {
    std::unique_lock<std::mutex> lock(mu);

    HashToStatusCollection.update_one(
        document{} << "hash" << hash << finalize,
        document{} << "$set" << open_document << "status" << status << "data" << result << close_document << finalize);
}

void TaskStorage::SetPartCount(const std::string& hash, int count) {
    std::unique_lock<std::mutex> lock(mu);

    auto result = PartCountsCollection.find_one(document{} << "hash" << hash << finalize);

    if (result) {
        PartCountsCollection.insert_one(document{} << "hash" << hash << "count" << count << finalize);
    }
}

void TaskStorage::AddPartResult(const std::string& hash, int partNumber, const std::string& result) {
    std::unique_lock<std::mutex> lock(mu);

    auto result = PartResultsCollection.update_one(
    document{} << "hash" << hash << "partNumber" << partNumber  finalize,
    document{} << "$set" << open_document << "result" << result << close_document << finalize,
    options
);
    int64_t count_docs = PartResultsCollection.count_documents(
        document{} << "hash" << hash << finalize
    );
    auto result = PartCountsCollection.find_one(document{} << "hash" << hash << finalize);
    pCount = result["count"].get_int32().value;
    double progress = (double)count_docs / pCount * 100;

    if (!result.empty())
    {
        if(!HashToStatusCollection.find_one(document{} << "hash" << hash << "status" << "READY" << finalize))
        {
            HashToStatusCollection.update_one(
                document{} << "hash" << hash << finalize,
                document{} << "$set" << open_document << "status" << "READY" << "data" << result << close_document << finalize);
            return;
        }
        else
        {
            HashToStatusCollection.insert_one(
                document{} << "hash" << hash << "status" << "READY" << "data" << result << close_document << finalize);
            return;
        }
    }
    auto hashStat = HashToStatusCollection.find_one(document{} << "hash" << hash << finalize);
    if (hashStat["status"].get_utf8().value.to_string() != "READY") {
        std::stringstream progressStr;
        progressStr << std::fixed << std::setprecision(1) << progress << "%";
        HashToStatusCollection.update_one(
            document{} << "hash" << hash << finalize,
            document{} << "$set" << open_document << "status" << "IN_PROGRESS" << "data" << progressStr.str() << close_document << finalize);
        return;
    }
    if (count_docs == partCounts[hash] && hashStat["status"].get_utf8().value.to_string() != "READY") {
        hashToStatus[hash] = StatusResponse{ "FAIL", {} } 
        HashToStatusCollection.update_one(
            document{} << "hash" << hash << finalize,
            document{} << "$set" << open_document << "status" << "FAIL" << "data" << "" << close_document << finalize);
        return;
    }
}