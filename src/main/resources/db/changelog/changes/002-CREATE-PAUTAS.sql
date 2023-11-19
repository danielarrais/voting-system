create table pautas
(
    id            int primary key,
    assembleia_id int null,
    descricao     varchar(255) not null,
    titulo        varchar(255) not null,
    foreign key (assembleia_id) references assembleias (id)
) ENGINE=INNODB;