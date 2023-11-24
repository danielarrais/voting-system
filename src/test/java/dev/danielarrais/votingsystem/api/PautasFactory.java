package dev.danielarrais.votingsystem.api;


import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;

import java.util.Arrays;

public class PautasFactory {
    private PautasFactory() {
    }

    public static PautaResponse[] getRetornoOk() {
        return Arrays.asList(getPautaResponse(1L, "Teste", "Teste"),
                getPautaResponse(2L, "Teste", "Teste"),
                getPautaResponse(3L, "Teste", "Teste"),
                getPautaResponse(4L, "Teste", "Teste"),
                getPautaResponse(5L, "Teste", "Teste")
        ).toArray(new PautaResponse[]{});
    }

    private static PautaResponse getPautaResponse(long id, String titulo, String descricao) {
        return PautaResponse.builder()
                .id(id).titulo(titulo).descricao(descricao)
                .build();
    }
}
