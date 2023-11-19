package dev.danielarrais.votingsystem.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class Assembleia {
    private Long id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataEncerramento;
    private List<Pauta> pautas;
}
