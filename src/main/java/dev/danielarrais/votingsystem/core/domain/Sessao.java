package dev.danielarrais.votingsystem.core.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Sessao {
    private final Long id;
    private final LocalDateTime dataInicio;
    private final LocalDateTime dataEncerramento;
    private final List<Voto> votos;

    @Builder
    public Sessao(Long id, LocalDateTime dataInicio, Integer duracao, List<Voto> votos) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataEncerramento = this.gerarDataEncerramento(duracao);
        this.votos = votos;
    }

    private LocalDateTime gerarDataEncerramento(Integer duracao) {
        return this.dataInicio.plusMinutes(duracao);
    }
}
