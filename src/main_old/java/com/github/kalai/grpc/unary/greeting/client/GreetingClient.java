package com.github.kalai.grpc.unary.greeting.client;

import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import com.proto.greet.GreetRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {

        System.out.println("Hello i am grpc client");
        // create a channel
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        System.out.println("Createing stub");
        //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(managedChannel);
        //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(managedChannel);
        GreetServiceGrpc.GreetServiceBlockingStub greetClent = GreetServiceGrpc.newBlockingStub(managedChannel);

        //Create a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirtName("Kalaiselvan")
                .setLastName("A")
                .build();
        // do the same for  a GreetRequest
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting).build();
        //call the RPC and get back a GreetResponse (protocol buffers)
        GreetResponse response = greetClent.greet(greetRequest);

        // print the response
        System.out.println(response.getResult());
        //do some io message
        System.out.println("Shutting down channel");
        managedChannel.shutdown();
    }
}
