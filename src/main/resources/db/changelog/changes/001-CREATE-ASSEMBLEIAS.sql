-- changeset danielarrais:1
create table assembleias
(
    id                int primary key,
    data_encerramento datetime(6) not null,
    data_inicio       datetime(6) not null
);
-- rollback drop table assembleias;