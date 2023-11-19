package dev.danielarrais.votingsystem.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class Sessao {
    private Long id;

    @Setter(AccessLevel.NONE)
    private LocalDateTime dataInicio;

    @Setter(AccessLevel.NONE)
    private LocalDateTime dataEncerramento;
    private List<Voto> votos;

    public boolean estaAtiva() {
        return LocalDateTime.now().isAfter(this.dataEncerramento);
    }
}
