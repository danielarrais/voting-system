package dev.danielarrais.votingsystem.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotoRequest {
    @NotNull(message = "O voto é obrigatório")
    private Boolean voto;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;
}
