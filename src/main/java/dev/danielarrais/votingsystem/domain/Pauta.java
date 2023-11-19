package dev.danielarrais.votingsystem.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Pauta {
    private  Long id;
    private String titulo;
    private String descricao;
    private Assembleia assembleia;
    private Sessao sessao;
}
