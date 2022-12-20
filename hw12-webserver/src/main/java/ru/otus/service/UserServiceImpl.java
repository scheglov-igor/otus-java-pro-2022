package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.model.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final DataTemplate<User> dataTemplate;
    private final TransactionManager transactionManager;

    public UserServiceImpl(TransactionManager transactionManager, DataTemplate<User> dataTemplate) {
        this.transactionManager = transactionManager;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Optional<User> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = dataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }


    @Override
    public Optional<User> findByLogin(String login) {
//        return transactionManager.doInReadOnlyTransaction(session -> {
//            var clientList = dataTemplate.findAll(session);
//            log.info("clientList:{}", clientList);
//            return Optional.ofNullable(clientList.size()>0 ? clientList.get(0) : null);
//        });

        return transactionManager.doInReadOnlyTransaction(session -> {
            var userList = dataTemplate.findByEntityField(session, "login", login);
            log.info("userList: {}", userList);
            return Optional.ofNullable(userList.size()>0 ? userList.get(0) : null);
        });


    }
}
