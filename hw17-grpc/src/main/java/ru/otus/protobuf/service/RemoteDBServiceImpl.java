package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.IntervalMessage;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {

    @Override
    public void getNumbers(IntervalMessage request, StreamObserver<NumberMessage> responseObserver) {
        for (int i = request.getFirstValue(); i <= request.getLastValue(); i++) {
            responseObserver.onNext(NumberMessage.newBuilder().setNum(i).build());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
}
