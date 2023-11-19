create table votos
(
    id        int primary key,
    voto      bool         not null,
    hora      datetime(6)  not null,
    pauta_id int          null,
    cpf       varchar(255) not null,
    foreign key (pauta_id) references votos (id)
);