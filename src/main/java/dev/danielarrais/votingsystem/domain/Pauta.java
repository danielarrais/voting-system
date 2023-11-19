package dev.danielarrais.votingsystem.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pauta {
    private  Long id;
    private String titulo;
    private String descricao;
    private Assembleia assembleia;
    private Sessao sessao;
}
