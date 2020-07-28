package com.github.kalai.grpc.unary.calculator.client;

import com.proto.greet.CalculatorServiceGrpc;
import com.proto.greet.SumRequest;
import com.proto.greet.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClent {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorServiceBlockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        SumRequest request = SumRequest.newBuilder().setFirtNum(3).setSecondNum(10).build();
        SumResponse response = calculatorServiceBlockingStub.sum(request);
        System.out.println("==========================result================================");
        System.out.println(response.getSumResult());
        channel.shutdown();
    }
}
