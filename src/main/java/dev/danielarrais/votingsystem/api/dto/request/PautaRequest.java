package dev.danielarrais.votingsystem.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PautaRequest {
    @NotBlank
    private String titulo;
    private String descricao;
}
