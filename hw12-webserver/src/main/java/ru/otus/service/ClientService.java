package ru.otus.service;

import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
