package com.github.kalai.grpc.unary.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(50051)
                .addService(new CalculatorServerImpl())
                .build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
