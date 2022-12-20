package ru.otus.server;

public interface MyWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
