package dev.danielarrais.votingsystem.api.mapper;

import dev.danielarrais.votingsystem.core.domain.Sessao;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class SessaoMapper {
    public Sessao convert(Integer duracao) {
        return Sessao.builder()
                .dataInicio(LocalDateTime.now())
                .duracao(duracao)
                .build();
    }
}
