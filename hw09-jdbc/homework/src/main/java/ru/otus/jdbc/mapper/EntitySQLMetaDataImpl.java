package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

//TODO
// что-то я запутался в дженериках...
// Вроде, только так работает без варнингов "unchecked"
// но в интерфейсе то дженерика нет...
// Это нормальная ситуация, когда интерфейс без дженерика реализуем с дженериком?
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData{

    private final EntityClassMetaData<T> entityClassMetaData;
    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    private StringBuilder createSelectSB() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");

        List<String> nameList = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .toList();

        sb.append(String.join(", ",  nameList));
        sb.append(" from ");
        sb.append(entityClassMetaData.getName());
        return sb;
    }


    @Override
    public String getSelectAllSql() {
        return createSelectSB().toString();
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder sb = createSelectSB();
        sb.append(" ");
        sb.append("where ");
        sb.append(entityClassMetaData.getIdField().getName());
        sb.append(" = ?");
        return sb.toString();
    }

    @Override
    public String getInsertSql() {
        List<String> nameList = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(entityClassMetaData.getName());
        sb.append(" (");
        sb.append(String.join(", ", nameList));
        sb.append(") ");
        sb.append("values ");
        sb.append("(");
        sb.append(String.join(", ", nameList.stream().map(s -> "?").toList()));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getUpdateSql() {

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(entityClassMetaData.getName());
        sb.append(" ");
        sb.append("set ");
        sb.append(entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", ")));

        sb.append(" ");
        sb.append("where ");
        sb.append(entityClassMetaData.getIdField().getName());
        sb.append("=?");

        return sb.toString();
    }
}