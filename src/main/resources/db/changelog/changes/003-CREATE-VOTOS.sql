create table votos
(
    id       int auto_increment primary key,
    voto     bool         not null,
    hora     timestamp not null,
    pauta_id int          null,
    cpf      varchar(255) not null,
    foreign key (pauta_id) references votos (id)
);