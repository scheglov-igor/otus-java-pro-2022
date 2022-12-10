package ru.otus.service;

import ru.otus.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(long id);
    Optional<User> findByLogin(String login);
}