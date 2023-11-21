package dev.danielarrais.votingsystem.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PautaRequest {
    @NotBlank(message = "O título da pauta é obrigatório")
    private String titulo;
    private String descricao;
}
