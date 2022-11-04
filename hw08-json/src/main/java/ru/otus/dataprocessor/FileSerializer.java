package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл

        try (FileWriter fileWriter = new FileWriter(fileName)){

            var gson = new Gson();
            gson.toJson(data, fileWriter);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
