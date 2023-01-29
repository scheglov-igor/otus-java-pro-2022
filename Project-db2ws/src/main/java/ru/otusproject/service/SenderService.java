package ru.otusproject.service;

import ru.otusproject.exception.CriticalStopException;

public interface SenderService {

    void start();
    void send(Long id);
}
