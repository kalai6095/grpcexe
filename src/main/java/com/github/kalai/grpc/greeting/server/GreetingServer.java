package com.github.kalai.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello gRPC");
        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .addService(ProtoReflectionService.newInstance())
                .build();
        server.start();

        // Thread th=new Thread();

        /*Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received shutdown Request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));*/

        server.awaitTermination();

    }
}
