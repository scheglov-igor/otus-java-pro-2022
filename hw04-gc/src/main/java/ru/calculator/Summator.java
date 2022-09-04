package ru.calculator;

import java.util.ArrayList;
import java.util.List;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;

//    понимаю, меняется логика работы. Но при текущем использовании коллекция и не нужна
//    private final List<Data> listValues = new ArrayList<>();
    private Integer listValuesSize = 0;

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
//        listValues.add(data);
//        if (listValues.size() % 6_600_000 == 0) {
//            listValues.clear();
//        }

        listValuesSize++;
        if (listValuesSize % 6_600_000 == 0) {
            listValuesSize = 0;
        }

        sum += data.getValue();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum);
//            someValue = Math.abs(someValue) + listValues.size();
            someValue = Math.abs(someValue) + listValuesSize;
        }
    }

    public Integer getSum() {
        return sum;
    }

    public Integer getPrevValue() {
        return prevValue;
    }

    public Integer getPrevPrevValue() {
        return prevPrevValue;
    }

    public Integer getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public Integer getSomeValue() {
        return someValue;
    }
}
