package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    //TODO решил передать EntityClassMetaData через конструктор здесь.
    // Еще можно через EntitySQLMetaData, но тогда нужно менять интерфейс,
    // но интерфейс EntitySQLMetaData по смыслу - только для создания текста запросов.
    // Короче, там совсем не надо.
    // Существуюет более красивые способы получить EntityClassMetaData?
    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    private T createInstance(ResultSet rs) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {
        int argsCount = entityClassMetaData.getAllFields().size();
        Object[] args = new Object[argsCount];
        for (int i = 0; i < argsCount; i++) {
            args[i] = rs.getObject(i+1);
        }
        return entityClassMetaData.getConstructor().newInstance(args);
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createInstance(rs);
                }
                return null;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var objList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    objList.add(createInstance(rs));
                }
                return objList;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T obj) {
        try {
            List<Object> objList = new ArrayList<>();
            for (Field field: entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                objList.add(field.get(obj));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), objList);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T obj) {
        try {
            List<Object> objList = new ArrayList<>();
            for (Field field: entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                objList.add(field.get(obj));
            }
            Field fieldId = entityClassMetaData.getIdField();
            fieldId.setAccessible(true);
            objList.add(fieldId.get(obj));

            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), objList);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
