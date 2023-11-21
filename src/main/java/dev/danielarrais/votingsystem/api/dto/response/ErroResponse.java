package dev.danielarrais.votingsystem.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ErroResponse {
    @Builder.Default
    private String data = Instant.now().toString();
    private int codigo;
	private String mensagem;
	private List<String> erros;
}
