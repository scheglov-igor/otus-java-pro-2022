package ru.otus.model;


import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
@Getter
@ToString
public class Client {

    @Id
    private final Long id;

    private final String name;

    @PersistenceCreator
    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
