package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

// а вот тут нужно добавлять интерфейс Copiable или можно сразу создать метод copy???
public class ObjectForMessage implements Copyable<ObjectForMessage> {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage copy() {

        var clone = new ObjectForMessage();

        var cloneData = new ArrayList<String>();
        for (String item : data) {
            cloneData.add(item);
        }
        clone.setData(cloneData);

        return clone;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
