package dev.danielarrais.votingsystem.api.mapper;

import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.core.domain.Pauta;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PautaMapper {
    public Pauta convert(PautaRequest pautaRequest) {
        return Pauta.builder()
                .titulo(pautaRequest.getTitulo())
                .descricao(pautaRequest.getDescricao())
                .build();
    }
}
