-- changeset danielarrais:2
create table sessoes
(
    id                int primary key,
    data_encerramento datetime(6) null,
    data_inicio       datetime(6) null
);
-- rollback drop table sessoes;