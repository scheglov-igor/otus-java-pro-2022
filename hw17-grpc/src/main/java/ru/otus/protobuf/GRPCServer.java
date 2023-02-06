package ru.otus.protobuf;


import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.RemoteDBServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteDBService = new RemoteDBServiceImpl();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteDBService).build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Recieved shutdown request");
            server.shutdown();
            System.out.println("Server stopped");
        }));

        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
