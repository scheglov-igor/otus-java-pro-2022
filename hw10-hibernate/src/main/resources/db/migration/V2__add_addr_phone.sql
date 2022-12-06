
create table address (
                         id bigint not null primary key,
                         street varchar(255),
                         client_id bigint not null
);

alter table address add constraint fk_address_client_id foreign key (client_id) references client;

create table phone (
                       id bigint not null primary key,
                       number varchar(255),
                       client_id bigint not null
);

alter table phone add constraint fk_phone_client_id foreign key (client_id) references client;
