package com.github.kalai.grpc.calculator.server;

import com.proto.greet.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServerImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        //super.sum(request, responseObserver);
        int num1 = request.getFirtNum();
        int num2 = request.getSecondNum();
        int sum = num1 + num2;
        System.out.println("Number from client is :" + num1 + " + " + num2);
        SumResponse response = SumResponse.newBuilder().setSumResult(sum).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        //super.primeNumberDecomposition(request, responseObserver);
        Long number = request.getNumber();
        Long divisor = 2L;
        while (number > 1) {
            if (number % divisor == 0) {
                number = number / divisor;
                responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(divisor)
                        .build());

            } else {
                divisor = divisor + 1;
                System.out.println(divisor);
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(final StreamObserver<ComputeAverageResponse> responseObserver) {
        //return super.computeAverage(responseObserver);
        StreamObserver<ComputeAverageRequest> streamObserver = new StreamObserver<ComputeAverageRequest>() {

            int sum = 0;
            int count = 0;

            @Override
            public void onNext(ComputeAverageRequest computeAverageRequest) {
                sum += computeAverageRequest.getNumber();
                count += 1;
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("sum value : " + sum + " Count : " + count);
                double average = (double) sum / count;
                responseObserver.onNext(ComputeAverageResponse.newBuilder().setAverage(average).build());
                responseObserver.onCompleted();
            }
        };
        return streamObserver;
    }

    @Override
    public StreamObserver<FindMaximumRequest> findMaximum(final StreamObserver<FindMaximumResponse> responseObserver) {

        return new StreamObserver<FindMaximumRequest>() {
            int currentMaximum = 0;

            @Override
            public void onNext(FindMaximumRequest findMaximumRequest) {
                int currentNumber = findMaximumRequest.getNumber();
                System.out.println("current number is :" + currentNumber + "--------" + currentMaximum);
                System.out.println("current number is :" + (currentNumber > currentMaximum));
                if (currentNumber > currentMaximum) {
                    System.out.println("inside if check");
                    currentMaximum = currentNumber;
                    responseObserver
                            .onNext(FindMaximumResponse
                                    .newBuilder()
                                    .setResult(currentMaximum)
                                    .build()
                            );
                } else {
                    System.out.println("outside if check");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver
                        .onNext(FindMaximumResponse
                                .newBuilder()
                                .setResult(currentMaximum)
                                .build()
                        );
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        Integer number = request.getNumber();
        if (number >= 0) {
            double numberRoot = Math.sqrt(number);
            responseObserver.onNext(SquareRootResponse.newBuilder().setNumberRoot(numberRoot).build());

        } else {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("The Number is not positive ")
                    .augmentDescription("Number send from client" + number).asRuntimeException());
        }
    }
}
