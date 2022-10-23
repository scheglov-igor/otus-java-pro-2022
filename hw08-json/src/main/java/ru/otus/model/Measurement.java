package ru.otus.model;


//Допустим, этот класс библиотечный, его нельзя менять
public final class Measurement {
    private final String name;
    private final double value;

    public Measurement(String name, double value) {
        this.name = name;
        this.value = value;
    }

    //через jackson у меня получилось работать только с такой аннотацией
/*
    @JsonCreator
    public Measurement(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        this.name = name;
        this.value = value;
    }
*/

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
