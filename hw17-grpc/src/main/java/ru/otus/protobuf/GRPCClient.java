package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private final Object monitor = new Object();
    private Integer lastNumber = 0;
    private Integer currentNumber = 0;

    public static void main(String[] args) throws InterruptedException {
        new GRPCClient().start();
    }

    private void start() throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var latch = new CountDownLatch(1);
        var newStub = RemoteDBServiceGrpc.newStub(channel);
        newStub.getNumbers(IntervalNumberRequest.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<NumberResponse>() {
            @Override
            public void onNext(NumberResponse um) {
                synchronized (monitor) {
                    System.out.printf("new value: %d\n", um.getNum());
                    lastNumber = um.getNum();
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("\n\nЯ все!");
                latch.countDown();
            }
        });

        scheduledThreadPoolExecutor(0, 50);

        latch.await();
        channel.shutdown();
    }

    private void scheduledThreadPoolExecutor(int firstValue, int lastValue) {
        for (int i = firstValue; i <= lastValue; i++) {
            synchronized (monitor) {
                currentNumber = currentNumber + lastNumber + 1;
                lastNumber = 0;
                System.out.printf("current value = %d\n", currentNumber);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
