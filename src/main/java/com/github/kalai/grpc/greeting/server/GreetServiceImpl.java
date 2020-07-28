package com.github.kalai.grpc.greeting.server;

import com.proto.greet.*;
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

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        //super.greetManyTimes(request, responseObserver);
        Greeting greeting = request.getGreeting();


        String firstName = greeting.getFirtName();
        //String result = "Hello " + firstName;
        try {
            for (int i = 0; i < 100; i++) {
                String result = " Hello " + firstName + ", response number =" + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(result)
                        .build();
                responseObserver.onNext(response);

                Thread.sleep(1000L);


            }
        } catch (InterruptedException e) {
            responseObserver.onError(e);
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }

    }

    @Override
    public StreamObserver<LongGreatRequest> longGreet(final StreamObserver<LongGreetResponse> responseObserver) {
        //return super.longGreet(responseObserver);
        StreamObserver<LongGreatRequest> requestObserver = new StreamObserver<LongGreatRequest>() {
            String result = "";

            @Override
            public void onNext(LongGreatRequest longGreatRequest) {
                result += "Hello " + longGreatRequest.getGreeting().getFirtName();
                System.out.println(longGreatRequest.getGreeting().getFirtName());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println(result);
                responseObserver
                        .onNext(
                                LongGreetResponse.newBuilder()
                                        .setResult(result)
                                        .build());
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(final StreamObserver<GreetEveryoneResponse> responseObserver) {
        // return super.greetEveryone(responseObserver);
        StreamObserver<GreetEveryoneRequest> requestObserver = new StreamObserver<GreetEveryoneRequest>() {
            @Override
            public void onNext(GreetEveryoneRequest value) {
                String result = value.getGreeting().getFirtName();
                System.out.println("got request form client" + result);
                GreetEveryoneResponse response = GreetEveryoneResponse
                        .newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}
