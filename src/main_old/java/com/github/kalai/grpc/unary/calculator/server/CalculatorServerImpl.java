package com.github.kalai.grpc.unary.calculator.server;

import com.proto.greet.CalculatorServiceGrpc;
import com.proto.greet.SumRequest;
import com.proto.greet.SumResponse;
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
}
