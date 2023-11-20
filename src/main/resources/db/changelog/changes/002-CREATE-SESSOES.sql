create table sessoes
(
    id                int auto_increment primary key,
    data_encerramento timestamp null,
    data_inicio       timestamp null,
    pauta_id          int null,
    foreign key (pauta_id) references pautas (id)
);