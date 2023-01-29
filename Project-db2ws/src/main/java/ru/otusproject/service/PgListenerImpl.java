package ru.otusproject.service;




import java.net.URISyntaxException;
import java.sql.SQLException;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;

import com.impossibl.postgres.jdbc.PGDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * только слушает изменения,
 * только получает id из базы и передает на отправку
 *
 * docs: http://impossibl.github.io/pgjdbc-ng/docs/snapshot/user-guide/#extensions-notifications
 */
@Service
public class PgListenerImpl implements PgListener {

    private static final Logger logger = LoggerFactory.getLogger(PgListenerImpl.class);

    private final SenderService senderService;
    private final String url;
    private final String username;
    private final String password;

    public PgListenerImpl(SenderService senderService,
                          @Value("${spring.datasource.url}") String url,
                          @Value("${spring.datasource.username}") String username,
                          @Value("${spring.datasource.password}") String password) {
        this.senderService = senderService;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private PGDataSource buildPgDataSource(String url, String username, String password) throws URISyntaxException {

        String thisUrl = url;
        System.out.println("thisUrl = " + thisUrl);

        thisUrl = thisUrl.substring(thisUrl.indexOf("://")+3);
        System.out.println("thisUrl = " + thisUrl);

        String host = thisUrl.substring(0, thisUrl.indexOf(":"));
        System.out.println("host = " + host);

        int port = Integer.valueOf(thisUrl.substring(thisUrl.indexOf(":")+1, thisUrl.indexOf("/")));
        System.out.println("port = " + port);

        String databaseName = thisUrl.substring(thisUrl.indexOf("/")+1);
        System.out.println("databaseName = " + databaseName);

        System.out.println("username = " + username);
        System.out.println("password = " + password);


        PGDataSource pgDataSource = new PGDataSource();
        pgDataSource.setHost(host);
        pgDataSource.setPort(port);
        pgDataSource.setDatabaseName(databaseName);
        pgDataSource.setUser(username);
        pgDataSource.setPassword(password);

        return pgDataSource;
    }




    public void startListen() {
        logger.info("starting listener...");
        var pgNotificationListener = new PGNotificationListener() {

            /**
             * ловим уведомление об изменении
             */
            @Override
            public void notification(int processId, String channelName, String payload) {
                System.out.println("Received Notification: " + processId + ", " + channelName + ", " + payload);
                System.out.println("payload = " + payload);

                Long id = Long.valueOf(payload);


//                var tlgSendGcTb = tlgSendGcTbOptional.orElseThrow(
//                        () -> new CriticalStopException("no row in DB! id=" + id)
//                );

                System.out.println("!!!!!!!!!!!!!!!");
                senderService.send(id);
            }

            @Override
            public void closed() {
                //TODO
                // initiate reconnection & restart listening
            }

        };


//TODO так в руководстве предлагается создать подключение

//        PGDataSource pgDataSource = (PGDataSource) dataSource;

        //OK
//        PGDataSource pgDataSource = new PGDataSource();
//        pgDataSource.setHost("localhost");
//        pgDataSource.setPort(5430);
//        pgDataSource.setDatabaseName("demoDB");
//        pgDataSource.setUser("usr");
//        pgDataSource.setPassword("pwd");



        PGConnection connection;
        try {
            connection = (PGConnection) buildPgDataSource(url, username, password).getConnection();
            connection.addNotificationListener(pgNotificationListener);

            final var statement = connection.createStatement();

            statement.execute("LISTEN notify_channel_tlg_send_gc_tb");
            statement.close();

//            var time = 60 * 60 * 1000;
//            System.out.println("Will sleep for $time milliseconds...");
//
//            try {
//                Thread.sleep (time);
//            } catch (Throwable thrown) {
//                thrown.printStackTrace (System.err);
//            } finally {
//                connection.close ();
//            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        //TODO connection.close ();

    }


}

