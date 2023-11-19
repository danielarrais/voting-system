package dev.danielarrais.votingsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Assembleia {
    private Long id;
    private LocalDateTime dataDeInicio;
    private LocalDateTime dataDeEncerramento;
    private List<Pauta> pautas;
}
