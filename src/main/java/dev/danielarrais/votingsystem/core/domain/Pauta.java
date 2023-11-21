package dev.danielarrais.votingsystem.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Pauta {
    private Long id;
    private String titulo;
    private String descricao;
}
