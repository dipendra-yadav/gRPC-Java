package com.github.aspiredipendra.grpc.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.*;


import java.io.IOException;

public class CalculatorServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Grpc CalculatorServer up!");
        Server server = ServerBuilder.forPort(50052)
                .addService(new CalculatorServiceImpl())
                .addService(ProtoReflectionService.newInstance()) // gRPC reflection
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));

        server.awaitTermination();
    }
}