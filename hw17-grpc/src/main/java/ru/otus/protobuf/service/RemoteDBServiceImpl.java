package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.IntervalNumberRequest;
import ru.otus.protobuf.generated.NumberResponse;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {

    @Override
    public void getNumbers(IntervalNumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        for (int i = request.getFirstValue(); i <= request.getLastValue(); i++) {
            responseObserver.onNext(NumberResponse.newBuilder().setNum(i).build());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
}
