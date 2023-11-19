-- changeset danielarrais:3
create table pautas
(
    id            int primary key,
    assembleia_id int       null,
    sessao_id     int       null,
    descricao     varchar(255) not null,
    titulo        varchar(255) not null,
    constraint UK_ihiuwxvshcjlu0j1ecsw1xmhr
        unique (sessao_id),
    constraint FK25yspbvuglwv5td6mmwsn0ht6
        foreign key (sessao_id) references sessoes (id),
    constraint FKcclxrhix34vv5baoiwbaopgsn
        foreign key (assembleia_id) references assembleias (id)
);
-- rollback drop table pautas;