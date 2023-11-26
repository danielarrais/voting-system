package dev.danielarrais.votingsystem.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PautaRequest {
    @NotBlank(message = "O título da pauta é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição da pauta é obrigatório")
    private String descricao;
}
