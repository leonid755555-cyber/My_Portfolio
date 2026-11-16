#include <iostream>
#include <memory>
#include <thread>
#include "main.hpp"
#include "httplib.h"
extern mongocxx::collection globals = {};
extern mongocxx::options::update options = {};
extern std::shared_ptr<TaskQueue> taskQueue = {};
extern std::shared_ptr<TaskStorage> GlobalTaskStorage = {};
extern std::shared_ptr<RoundRobin> LoadBalancer = {};

int main() {
    mongocxx::instance inst{};
    
    mongocxx::uri uri("mongodb://mongo1:27017,mongo2:27017,mongo3:27017/?replicaSet=rs0");
    mongocxx::client client(uri);
    
    auto db = client["testdb"];
    globals = db["globals"];
    mongocxx::options::update options;
    options.upsert(true);

    if(!static_cast<bool>(globals.find_one(document{} << "name" << "max_id" << finalize)))
    {   
        auto doc = document{}
            << "name" << "max_id"
            << "value" << 0
            << finalize;
        globals.insert_one(doc.view());
    }

    dispatcher = std::make_shared<TaskDispatcher>(taskQueue);
    dispatcher->Start();

    Start();

    return 0;
}