package dev.danielarrais.votingsystem.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class Voto {
    private Long pautaId;
    private String cpf;
    private LocalDateTime hora;
    private Boolean voto;
}
