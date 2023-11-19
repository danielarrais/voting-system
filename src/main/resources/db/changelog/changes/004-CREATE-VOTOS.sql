-- changeset danielarrais:4
create table votos
(
    id        int primary key,
    voto      bool          not null,
    hora      datetime(6)  not null,
    sessao_id int       null,
    cpf       varchar(255) not null,
    constraint FKpeo8sv13nyl4tvo2uq9mgf3tp
        foreign key (sessao_id) references sessoes (id)
);
-- rollback drop table votos;