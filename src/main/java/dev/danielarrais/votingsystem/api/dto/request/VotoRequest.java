package dev.danielarrais.votingsystem.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VotoRequest {
    @NotNull(message = "O voto é obrigatório")
    private Boolean voto;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;
}
