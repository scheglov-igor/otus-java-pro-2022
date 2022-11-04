package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {

        var file = new File(ClassLoader.getSystemResource(fileName).getFile());

        // c jackson у меня не получилось без изменения Measurement. Про
        // TypeReference typeReference = new TypeReference<List<YourClass>>() { };
        // нашел быстро, но там нужно либо добавить дефолтный конструктор (это неправильный подход для final полей),
        // либо проставить
        // @JsonCreator
        // public Measurement(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        // над существующим конструктором. Нормальное решение, но условия задачи не проходят.
        //
        // буду очень благодарен за подсказку, как надо было правильно готовить jackson!

        Gson gson = new Gson();

        try(InputStream initialStream = new FileInputStream(file);
            Reader targetReader = new InputStreamReader(initialStream)) {
            Type collectionType = new TypeToken<ArrayList<Measurement>>(){}.getType();
            return gson.fromJson(targetReader, collectionType);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

