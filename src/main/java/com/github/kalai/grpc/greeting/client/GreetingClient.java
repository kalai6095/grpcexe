package com.github.kalai.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {


    public void run(String hostname, Integer port) {

        System.out.println(hostname + "------------------------" + port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(hostname, port)
                .usePlaintext()
                .build();
        doUnaryCall(channel);
        //doServerStreamingCall(channel);
        //doClientStreamingCall(channel);
        doBiDiStreamcall(channel);


        channel.shutdown();


    }

    private void doBiDiStreamcall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        final CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<GreetEveryoneRequest> requestObserver = asyncClient
                .greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
                    @Override
                    public void onNext(GreetEveryoneResponse value) {
                        System.out.println("value from server     ----->   " + value.getResult());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("serrver is done  sending data");
                    }
                });

        try {
            for (int i = 0; i < 10; i++) {
                requestObserver.onNext(GreetEveryoneRequest.newBuilder().setGreeting(
                        Greeting.newBuilder()
                                .setFirtName("    Client Request" + i)
                                .setLastName("    Client Stream " + i)
                                .build()
                ).build());
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestObserver.onCompleted();

        try {
            latch.await(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doClientStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);


        final CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<LongGreatRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse longGreetResponse) {
                //we get response from the server
                System.out.println("received a response from the server");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                //server will send a data on completion
                System.out.println("request done");
                latch.countDown();
            }
        });
        try {
            for (int i = 0; i < 10; i++) {
                requestObserver.onNext(LongGreatRequest.newBuilder().setGreeting(
                        Greeting.newBuilder()
                                .setFirtName("Client Request: " + i)
                                .setLastName(" Stream")
                                .build()
                ).build());
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("done sending data");
        requestObserver.onCompleted();
        try {
            latch.await(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClent = GreetServiceGrpc.newBlockingStub(channel);

        GreetManyTimesRequest greetManyTimesRequest =
                GreetManyTimesRequest.newBuilder()
                        .setGreeting(Greeting.newBuilder()
                                .setFirtName("kalaiselvan gRPC")
                                .setLastName("Server Stream")
                                .build()
                        ).build();
        //we Stream the response in a blocking manner
       /* greetClent.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse);
                });*/
    }

    private void doUnaryCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClent = GreetServiceGrpc.newBlockingStub(channel);

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
    }


    public static void main(String[] args) {
        Integer port = 0;
        String hostname = "";
        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                System.err.println("Usage: [port [hostname]]");
                System.err.println("");
                System.err.println("  port      The listen port. Defaults to " + port);
                System.err.println("  hostname  The name clients will see in greet responses. ");
                System.err.println("            Defaults to the machine's hostname");
                System.exit(1);
            }
        }
        if (args.length >= 2) {
            hostname = args[1];
        }

        System.out.println("Hello i am grpc client");
        // create a channel

        System.out.println("Createing stub");
        GreetingClient main = new GreetingClient();
        main.run(hostname, port);
        //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(managedChannel);
        //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(managedChannel);


    }
}
