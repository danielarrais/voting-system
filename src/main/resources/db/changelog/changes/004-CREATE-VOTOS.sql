create table votos
(
    id        int primary key,
    voto      bool         not null,
    hora      datetime(6)  not null,
    sessao_id int          null,
    cpf       varchar(255) not null,
    foreign key (sessao_id) references sessoes (id)
) ENGINE = INNODB;