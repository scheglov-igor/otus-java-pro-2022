create table client
(
    id bigserial not null primary key,
    name varchar(50)
);

insert into client(name) values ('первый клиент');