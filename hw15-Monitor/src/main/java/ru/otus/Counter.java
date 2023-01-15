package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {

    private static final Logger log = LoggerFactory.getLogger(Counter.class);

    public static void main(String[] args) {
        new Counter().start();
    }

    private int number = 1;

    private int increment = 1;

    private boolean doIncrement = false;

    private String lastThread = "Thread2";


    private void start() {
        StringBuilder s1 = new StringBuilder();
        Thread thread1 = new Thread(() -> printInString(s1),"Thread1");
        thread1.start();
        StringBuilder s2 = new StringBuilder(" ");
        Thread thread2 = new Thread(() -> printInString(s2),"Thread2");
        thread2.start();
    }

    private synchronized void printInString(StringBuilder sb) {
        while(!Thread.currentThread().isInterrupted()) {
            try {

                while (Thread.currentThread().getName().equals(lastThread)) {
                    this.wait();
                }

                sb.append(number);
                sb.append(" ");

                if(doIncrement) {
                    number = number + increment;
                    if (number >= 10) {
                        increment = -1;
                    }
                    if (number <= 0) {
                        increment = 1;
                    }
                }
                doIncrement = !doIncrement;

                lastThread = Thread.currentThread().getName();

                log.info(sb.toString());

                sleep();
                notifyAll();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
