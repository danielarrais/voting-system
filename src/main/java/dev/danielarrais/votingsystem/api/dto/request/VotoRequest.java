package dev.danielarrais.votingsystem.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VotoRequest {
    @NotNull
    private Boolean voto;
    @NotNull
    private String cpf;
}
