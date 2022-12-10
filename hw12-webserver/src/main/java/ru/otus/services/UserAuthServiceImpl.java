package ru.otus.services;

import ru.otus.service.UserService;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserService userService;

    public UserAuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userService.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
