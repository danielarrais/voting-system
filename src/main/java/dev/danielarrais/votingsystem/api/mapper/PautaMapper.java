package dev.danielarrais.votingsystem.api.mapper;

import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;
import dev.danielarrais.votingsystem.core.domain.Pauta;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PautaMapper {
    public Pauta convert(PautaRequest pautaRequest) {
        return Pauta.builder()
                .titulo(pautaRequest.getTitulo())
                .descricao(pautaRequest.getDescricao())
                .build();
    }

    public List<PautaResponse> convert(List<Pauta> pautas) {
        return pautas.stream()
                .map(PautaMapper::getPautaResponse)
                .toList();
    }

    private PautaResponse getPautaResponse(Pauta pauta) {
        return PautaResponse.builder()
                .id(pauta.getId())
                .titulo(pauta.getTitulo())
                .descricao(pauta.getDescricao())
                .build();
    }
}
