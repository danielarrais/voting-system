package dev.danielarrais.votingsystem.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Voto {
    private Long id;
    private String CPF;
    private LocalDateTime hora;
    private Boolean voto;
}
