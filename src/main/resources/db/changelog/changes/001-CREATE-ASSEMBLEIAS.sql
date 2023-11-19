create table assembleias
(
    id                int primary key,
    data_encerramento datetime(6) not null,
    data_inicio       datetime(6) not null
) ENGINE=INNODB;