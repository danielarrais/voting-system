package dev.danielarrais.votingsystem.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class Voto {
    private Long id;
    private String CPF;
    private LocalDateTime hora;
    private Boolean voto;
}
