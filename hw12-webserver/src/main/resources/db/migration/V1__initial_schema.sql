create table client
(
    id bigserial not null primary key,
    name varchar(50)
);

create table my_user (
     id bigserial not null primary key,
     name varchar(255),
     login varchar(255),
     password varchar(255)
);

insert into my_user(name, login, password) values ('Крис Гир', 'user1', '11111');

insert into client(name) values ('первый клиент');