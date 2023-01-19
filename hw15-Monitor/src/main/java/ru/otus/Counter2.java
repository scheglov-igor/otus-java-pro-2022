package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter2 {

    private static final Logger log = LoggerFactory.getLogger(Counter.class);

    public static void main(String[] args) {
        new Counter2().start();
    }

    private int number = 1;

    private int increment = 1;

    private String lastThread = "Thread2";

//     0 - print number
//     1 - print space
//     2 - print space
//     3 - print number and set status = 0 and increment number
    private int printOrIncrementStatus = 0;

    private void start() {
        String s1 = "";
        Thread thread1 = new Thread(() -> printInString(s1),"Thread1");
//        Thread thread1 = new Thread(this::printInConsole,"Thread1");
        thread1.start();
        String s2 = "";
        Thread thread2 = new Thread(() -> printInString(s2),"Thread2");
//        Thread thread2 = new Thread(this::printInConsole,"Thread2");
        thread2.start();
    }

    private synchronized void printInString(String str) {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (Thread.currentThread().getName().equals(lastThread)) {
                    this.wait();
                }
                switch (printOrIncrementStatus) {
                    case 0:
                        str+=number;
                        printOrIncrementStatus++;
                        break;
                    case 1:
                    case 2:
                        str+=" ";
                        printOrIncrementStatus++;
                        break;
                    case 3:
                        str+=number;
                        number = number + increment;
                        if(number >= 10) {
                            increment = -1;
                        }
                        if(number <= 0) {
                            increment = 1;
                        }
                        printOrIncrementStatus = 0;
                }

                lastThread = Thread.currentThread().getName();

                log.info(str);

                sleep();
                notifyAll();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private synchronized void printInConsole() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (Thread.currentThread().getName().equals(lastThread)) {
                    this.wait();
                }

                switch (printOrIncrementStatus) {
                    case 0:
                        log.info("{}", number);
                        printOrIncrementStatus++;
                        break;
                    case 1:
                    case 2:
                        log.info(" ");
                        printOrIncrementStatus++;
                        break;
                    case 3:
                        log.info("{}", number);
                        number = number + increment;
                        if(number >= 10) {
                            increment = -1;
                        }
                        if(number <= 0) {
                            increment = 1;
                        }
                        printOrIncrementStatus = 0;
                }

                lastThread = Thread.currentThread().getName();
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
