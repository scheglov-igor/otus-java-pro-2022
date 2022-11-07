package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>{

    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }


    private String name = null;
    @Override
    public String getName() {
        if(name == null) {
            name = clazz.getSimpleName();
        }
        return name;
    }


    private Constructor<T> constructor = null;
    @Override
    //TODO мне совсем не нравится такая реализация. Конструктора со всеми полями может не быть,
    // порядок полей может быть другим.
    // кажется, лучше через сеттеры добавлять поля по одному. надежнее.
    // ...
    // Как вариант решения проблемы с конструктором -
    // завязать поля для селекта на выбранный конструктор,
    // запрашивать только то, что мы можем запихнуть в конструктор.
    // Но в конструкторе только типы полей, не названия...
    // короче, не знаю, как это нормально сделать. =/
    public Constructor<T> getConstructor() {
        if(constructor == null) {
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        return constructor;
    }


    private Field idField = null;

    //TODO Я не совсем понимаю, как работают лябмды с внешними переменными
    // строчка 65 ".map(f -> idField = f)"
    // если idField перенести внутрь метода getIdField - будет ошибка
    // Variable used in lambda expression should be final or effectively final
    // ошибка - это вполне ожидаемое поведение, локальные переменные должны быть финальными
    // ...
    // А здесь переменная не локальная - её нормально менять из лямбды? Это не приведет
    // к каким-то нехорошим последствиям?
    @Override
    public Field getIdField() {
        if(idField == null) {
            long idCount = getAllFields().stream()
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .map(f -> idField = f)
                    .count();

            if (idCount < 1) {
                throw new RuntimeException("no @Id field");
            }
            if (idCount > 1) {
                throw new RuntimeException("to many @Id fields");
            }
        }

        return idField;
    }


    private List<Field> allFieldsList = null;
    @Override
    public List<Field> getAllFields() {
        if(allFieldsList == null) {
            allFieldsList = Arrays.asList(clazz.getDeclaredFields());
        }

        return allFieldsList;
    }


    private List<Field> fieldsWithoutIdList = null;

    @Override
    public List<Field> getFieldsWithoutId() {

        if(fieldsWithoutIdList == null) {
            fieldsWithoutIdList = getAllFields().stream()
                    .filter(f -> !f.equals(getIdField()))
                    .toList();
        }

        return fieldsWithoutIdList;
    }
}
