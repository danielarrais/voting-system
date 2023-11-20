package dev.danielarrais.votingsystem.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Resultado {
    private String resultado;
    private int votosFavoraveis;
    private int votosContrarios;
}
