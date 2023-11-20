package dev.danielarrais.votingsystem.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    @Builder.Default
    private String date = Instant.now().toString();
    private int code;
	private String message;
	private List<String> details;
}
