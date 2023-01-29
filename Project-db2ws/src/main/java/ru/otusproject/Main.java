package ru.otusproject;

import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otusproject.service.PgListener;
import ru.otusproject.service.SenderService;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        var context = SpringApplication.run(Main.class, args);

        SenderService senderService = context.getBean(SenderService.class);
        senderService.start();

        PgListener pgListener = context.getBean(PgListener.class);
        pgListener.startListen();

        //TODO проверить если обрыв соединения листенера????

    }
}