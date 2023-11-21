package dev.danielarrais.votingsystem.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PautaResponse {
    private Long id;
    private String titulo;
    private String descricao;
}
