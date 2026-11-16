#include <iostream>
#include <memory>
#include <amqpcpp.h>
#include <amqpcpp/linux_tcp.h>
#include <ev.h>

#include "httplib.h"

#include "main.hpp"

RabbitMQManager::RabbitMQManager() {
    
    loop = ev_loop_new(EVFLAG_AUTO);
    handler = new AMQP::LibEvHandler(loop);
    
    connection = std::make_shared<AMQP::TcpConnection>(handler, 
        AMQP::Address("amqp://guest:guest@rabbitmq/"));
    
    channel = std::make_shared<AMQP::TcpChannel>(connection.get());
    
    declareQueues();
}

void RabbitMQManager::declareQueues() {
    channel->declareQueue("hash_crack_results", AMQP::durable, AMQP::arguments{{"x-message-ttl", 5000}})
        .onSuccess([this]() {
            channel->consume("hash_crack_results")
                .onReceived([this](const AMQP::Message &message, uint64_t deliveryTag, bool redelivered) {
                    try {
                    ResultHandler(message, deliveryTag);
                    channel->ack(deliveryTag);
                    }
                    catch (const std::exception& e) {
                        std::cerr << e.what() << std::endl;
                        channel.reject(deliveryTag, true);
                    }
                });
        });
    
    channel->declareQueue("worker_registration", AMQP::durable, AMQP::arguments{{"x-message-ttl", 5000}})
        .onSuccess([this]() {
            channel->consume("worker_registration")
                .onReceived([this](const AMQP::Message &message, uint64_t deliveryTag, bool redelivered) {
                    try {
                    WorkerRegisterHandler(message, deliveryTag);
                    channel->ack(deliveryTag);
                    }
                    catch (const std::exception& e) {
                        std::cerr << e.what() << std::endl;
                        channel.reject(deliveryTag, true);
                    }
                });
        });
}

void RabbitMQManager::run() {
    std::cout << "Manager connected to RabbitMQ and waiting for messages from workers" << std::endl;
    ev_run(loop, 0);
}

RabbitMQManager::~RabbitMQManager() {
    ev_loop_destroy(loop);
    delete handler;
}

void Start(std::shared_ptr<TaskQueue> taskQueue, std::shared_ptr<TaskDispatcher> taskDispatcher) {
    RabbitMQManager manager(taskQueue, taskDispatcher);
    manager.run();

    httplib::Server svr;

    svr.Post("/internal/api/manager/hash/crack/result", [&](const httplib::Request& req, httplib::Response& res) {
        ResultHandler(req, res);
        });

    svr.Post("/internal/api/worker/register", [&](const httplib::Request& req, httplib::Response& res) {
        WorkerRegisterHandler(req, res);
        });

    std::cout << "Manager listening on port :8080" << std::endl;
    if (!svr.listen("manager", 8080)) {
        std::cerr << "Server error: Failed to start server" << std::endl;
    }
}