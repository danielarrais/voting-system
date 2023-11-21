package dev.danielarrais.votingsystem.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Resultado {
    private String resultado;
    private Long pautaId;
    private int votosFavoraveis;
    private int votosContrarios;
}
