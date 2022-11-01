package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {

        // у меня огромный вопрос, как эти стримы научиться понимать?
        // сейчас я с горем пополам с помощью гугла нарыл решение, вроде даже разобрался, как оно работает,
        // но вот это всё какая-то магия!
        // может, книжка какая для этого есть, вебинар, курс?...

        // курс от отуса реально помогает, понимание дженериков, лямбд-монад, функциональных интерфейсов -
        // даёт основу для понимания процессов, раньше я бы плюнул и написал бы на циклах
        // может, просто практика нужна и понимание само придёт?

        // кстати, как обратная связь по курсу - возможно, стоит стримам уделить чуть больше внимания.

        return data.stream()
                .collect(
                    Collectors.groupingBy(
                            Measurement::getName,
                            () -> new TreeMap<>(String::compareTo),
                            Collectors.summingDouble(Measurement::getValue)
                    )
                );

    }
}
