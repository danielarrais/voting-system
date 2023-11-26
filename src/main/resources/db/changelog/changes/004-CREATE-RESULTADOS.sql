create table resultados
(
    id               int auto_increment primary key,
    votos_contrarios int         not null default 0,
    votos_favoraveis int         not null default 0,
    resultado        varchar(20) not null,
    pauta_id         int         null,
    foreign key (pauta_id) references pautas(id)
);