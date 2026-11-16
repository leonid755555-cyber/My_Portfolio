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
    
    channel->declareQueue("complite_crack_task", AMQP::durable, AMQP::arguments{{"x-message-ttl", 5000}})
        .onSuccess([this]() {
            channel->consume("complite_crack_task")
                .onReceived([this](const AMQP::Message &message, uint64_t deliveryTag, bool redelivered) {
                    try {
                    CreateCrackTaskHandler(message, deliveryTag);
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
    std::cout << "Worker connected to RabbitMQ and waiting for messages from Manager" << std::endl;
    ev_run(loop, 0);
}

RabbitMQManager::~RabbitMQManager() {
    ev_loop_destroy(loop);
    delete handler;
}

void StartServer() {
    RabbitMQManager manager();
    manager.run();

    std::cout << "Started worker server on port " << cfg.Port
        << " with " << 1 << " max workers" << std::endl;
}
