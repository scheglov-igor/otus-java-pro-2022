package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorEvenSecondThrower implements Processor{

    public ProcessorEvenSecondThrower(TimeService ts) {
        this.ts = ts;
    }

    private final TimeService ts;

    @Override
    public Message process(Message message) {

        //тут возможны разногласия, какую секунду считать чётной. Первая секунда здесь это вторая на нормальных часах.
        if(ts.getDate().getSecond()%2 == 0) {
            System.out.println("%2 !!!");
           throw new RuntimeException("evenSecond exception: " + ts.getDate().toString());
        }
        return message;
    }
}