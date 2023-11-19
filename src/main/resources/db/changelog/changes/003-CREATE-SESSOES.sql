create table sessoes
(
    id                int primary key,
    data_encerramento datetime(6) null,
    data_inicio       datetime(6) null,
    pauta_id          int null,
    foreign key (pauta_id) references pautas (id)
) ENGINE=INNODB;