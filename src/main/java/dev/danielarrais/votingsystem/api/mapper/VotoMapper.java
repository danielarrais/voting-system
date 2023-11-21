package dev.danielarrais.votingsystem.api.mapper;

import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.core.domain.Voto;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class VotoMapper {

    public Voto convert(Long pautaId, VotoRequest votoRequest) {
        return Voto.builder()
                .pautaId(pautaId)
                .voto(votoRequest.getVoto())
                .cpf(votoRequest.getCpf())
                .hora(LocalDateTime.now())
                .build();
    }
}
