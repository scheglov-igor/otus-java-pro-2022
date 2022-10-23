package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.*;

// 1. я правильно понимаю, что тут нужно было менять сам класс, а не наследоваться от него?
public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Deque<Message>> messageDequeMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Deque<Message> messageDeque = messageDequeMap.computeIfAbsent(msg.getId(), l -> new ArrayDeque<>());
        messageDeque.push(msg.copy());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageDequeMap.get(id).poll());
    }
}
