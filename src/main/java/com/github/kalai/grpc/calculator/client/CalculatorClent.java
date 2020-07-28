package com.github.kalai.grpc.calculator.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClent {

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        //doUnaryCall(channel);
        //doServerStream(channel);
        //doClientStream(channel);
        //doBiDiStream(channel);
        dosquarerootCall(channel);

        channel.shutdown();
    }

    private void dosquarerootCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);
        int number = 3;
        try {
            SquareRootResponse response = blockingStub
                    .squareRoot(
                            SquareRootRequest
                                    .newBuilder()
                                    .setNumber(number)
                                    .build()
                    );
            System.out.println("square root value is ");
            System.out.println(response.getNumberRoot());
        } catch (StatusRuntimeException e) {
            System.out.println("exception occured");
            e.printStackTrace();
        }


    }

    private void doBiDiStream(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClent = CalculatorServiceGrpc.newStub(channel);
        final CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<FindMaximumRequest> requestObserver = asyncClent
                .findMaximum(new StreamObserver<FindMaximumResponse>() {
                    @Override
                    public void onNext(FindMaximumResponse value) {
                        System.out.println("Received a response from the server");
                        System.out.println(value.getResult());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onCompleted() {

                        latch.countDown();
                    }
                });
        Integer[] arr = {10, 45, 72, 86, 92, 99, 30, 37};
        try {

            for (int i = 0; i < arr.length; i++) {
                requestObserver.onNext(
                        FindMaximumRequest.newBuilder().build().newBuilder()
                                .setNumber(arr[i])
                                .build()
                );
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

    private void doClientStream(ManagedChannel channel) {

        CalculatorServiceGrpc.CalculatorServiceStub asyncClent = CalculatorServiceGrpc.newStub(channel);
        final CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<ComputeAverageRequest> requestObserver = asyncClent
                .computeAverage(new StreamObserver<ComputeAverageResponse>() {
                    @Override
                    public void onNext(ComputeAverageResponse computeAverageResponse) {
                        System.out.println("Received a response from the server");
                        System.out.println(computeAverageResponse.getAverage());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onCompleted() {
                        latch.countDown();
                    }
                });
        Integer[] arr = {10, 45, 72, 86, 92, 99, 30, 37};
        for (int i = 0; i < arr.length; i++) {
            requestObserver.onNext(
                    ComputeAverageRequest.newBuilder()
                            .setNumber(arr[i])
                            .build()
            );
        }
        requestObserver.onCompleted();
        try {
            latch.await(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doServerStream(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorServiceBlockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        Long number = 567890980L;
        /*calculatorServiceBlockingStub.primeNumberDecomposition(
                PrimeNumberDecompositionRequest
                        .newBuilder()
                        .setNumber(number)
                        .build()
        ).forEachRemaining(e -> {
            System.out.println(e.getPrimeFactor());
        });*/
    }

    private void doUnaryCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorServiceBlockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        SumRequest request = SumRequest.newBuilder().setFirtNum(3).setSecondNum(10).build();
        SumResponse response = calculatorServiceBlockingStub.sum(request);
        System.out.println("==========================result================================");
        System.out.println(response.getSumResult());
    }

    public static void main(String[] args) {

        CalculatorClent main = new CalculatorClent();
        main.run();

    }
}
