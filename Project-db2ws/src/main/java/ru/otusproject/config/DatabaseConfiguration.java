package ru.otusproject.config;

import com.impossibl.postgres.jdbc.PGDataSource;
import com.impossibl.postgres.jdbc.PGDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URISyntaxException;


@Configuration
public class DatabaseConfiguration {
/*
    @Bean
    public DataSource dataSource()
    {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5430/demoDB";
        String username ="usr";
        String password = "pwd";

        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
*/
//
//    @Bean
//    @ConfigurationProperties("spring.datasource")
//    public DataSourceProperties dataSourceProperties() {
//        DataSourceProperties dsp = new DataSourceProperties();
//        return dsp;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        DataSource pgds = dataSourceProperties()
//                .initializeDataSourceBuilder()
//                .build();
//        return pgds;
//    }



//
//    @Bean
//    public PGDataSource pgDataSource(
//
//            @Value("${spring.datasource.url}") String url,
//            @Value("${spring.datasource.username}") String username,
//            @Value("${spring.datasource.password}") String password
//
//
//
//    ) throws URISyntaxException {
//
////        System.out.println("DatabaseConfiguration - dataSource = " + dataSource);
//
//        System.out.println("url = " + url);
//
//        url = url.substring(url.indexOf("://")+3);
//        System.out.println("url = " + url);
//
//        String host = url.substring(0, url.indexOf(":"));
//        System.out.println("host = " + host);
//
//        int port = Integer.valueOf(url.substring(url.indexOf(":")+1, url.indexOf("/")));
//        System.out.println("port = " + port);
//
//        String databaseName = url.substring(url.indexOf("/")+1);
//        System.out.println("databaseName = " + databaseName);
//
//        System.out.println("username = " + username);
//        System.out.println("password = " + password);
//
//
//        PGDataSource pgDataSource = new PGDataSource();
//        pgDataSource.setHost(host);
//        pgDataSource.setPort(port);
//        pgDataSource.setDatabaseName(databaseName);
//        pgDataSource.setUser(username);
//        pgDataSource.setPassword(password);
//
//        return pgDataSource;
//    }

}
