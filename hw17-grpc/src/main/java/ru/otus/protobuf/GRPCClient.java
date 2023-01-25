package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.IntervalMessage;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private final List<Integer> numList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        new GRPCClient().start();
    }

    private void start() throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        scheduledThreadPoolExecutor(0, 50);

        var latch = new CountDownLatch(1);
        var newStub = RemoteDBServiceGrpc.newStub(channel);
        newStub.getNumbers(IntervalMessage.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<NumberMessage>() {
            @Override
            public void onNext(NumberMessage um) {
                System.out.printf("new value: %d\n", um.getNum());
                numList.add(um.getNum());
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

        latch.await();
        System.out.println("numList: " + numList);
        channel.shutdown();
    }

    private void scheduledThreadPoolExecutor(int firstValue, int lastValue) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        AtomicInteger i = new AtomicInteger(firstValue);
        executor.scheduleAtFixedRate(() -> {

            int num = i.getAndIncrement();
            numList.add(1);
            System.out.printf("current value = %d\n", numList.stream().reduce(0, Integer::sum));

            if (i.get() > lastValue) {
                executor.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
