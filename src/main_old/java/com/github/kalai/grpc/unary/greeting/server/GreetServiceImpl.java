package com.github.kalai.grpc.unary.greeting.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        System.out.println("=============================got request from client===========================");
        //extract response
        Greeting greeting = request.getGreeting();


        String firstName = greeting.getFirtName();
        String result = "Hello " + firstName;

        //create response
        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

        //send the response
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();
        //super.greet(request, responseObserver);
    }
}
