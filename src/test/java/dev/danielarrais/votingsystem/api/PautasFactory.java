package dev.danielarrais.votingsystem.api;


import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;
import dev.danielarrais.votingsystem.core.domain.Pauta;

import java.util.Arrays;
import java.util.List;

public class PautasFactory {
    private PautasFactory() {
    }

    public static List<PautaResponse> getRetornoOk() {
        return Arrays.asList(getPautaResponse(1L, "pauta-teste-1", "pauta-para-teste-sucesso-1"),
                getPautaResponse(2L, "pauta-teste-2", "pauta-para-teste-sucesso-2"),
                getPautaResponse(3L, "pauta-teste-3", "pauta-para-teste-sucesso-3"),
                getPautaResponse(4L, "pauta-teste-4", "pauta-para-teste-sucesso-4"),
                getPautaResponse(5L, "pauta-teste-5", "pauta-para-teste-sucesso-5")
        );
    }

    private static PautaResponse getPautaResponse(long id, String titulo, String descricao) {
        return PautaResponse.builder()
                .id(id).titulo(titulo).descricao(descricao)
                .build();
    }
}
