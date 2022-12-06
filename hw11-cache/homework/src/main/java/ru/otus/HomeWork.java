package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();


// Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная
        HwCache<Long, Client> cache = new MyCache<>();

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, cache);
//        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, null);

        LocalDateTime t1 = LocalDateTime.now();
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            var c =dbServiceClient.saveClient(new Client("dbServiceFirst"));
            idList.add(c.getId());
        }
        LocalDateTime t2 = LocalDateTime.now();


        for (int j = 0; j < 100; j++) {
            for (Long id: idList) {
                var clientSecondSelected = dbServiceClient.getClient(id)
                        .orElseThrow(() -> new RuntimeException("Client not found" + id));
            }
        }
        LocalDateTime t3 = LocalDateTime.now();

        System.out.println("start insert" + t1);
        System.out.println("finish insert" + t2);

        System.out.println("start select" + t2);
        System.out.println("finish select" + t3);

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        clientSecond.setName("newClientName");
        dbServiceClient.saveClient(clientSecond);

        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
