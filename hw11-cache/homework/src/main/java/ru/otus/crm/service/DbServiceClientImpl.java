package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    private final HwCache<Long, Client> cache;
    private final Boolean useCache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<Long, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;

        this.cache = cache;
        useCache = cache != null;
    }


    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                if(useCache) {
                    cache.put(clientId, createdClient);
                    log.info("(i) put client to cache: {}", createdClient);
                }
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            if(useCache) {
                cache.put(client.getId(), client);
                log.info("(u) put client to cache: {}", client);
            }
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {

            if(useCache) {
                Client cachedClient = cache.get(id);
                if (cachedClient != null) {
                    log.info("get client from cache: {}", cachedClient);
                    return Optional.of(cachedClient);
                }
            }

            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client from DB: {}", clientOptional);

            if(useCache) {
                if (clientOptional.isPresent()) {
                    cache.put(id, clientOptional.get());
                    log.info("(g) put client to cache: {}", clientOptional.get());
                }
            }
            return clientOptional;

        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            // мне кажется, тут от кэширования не будет пользы
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
