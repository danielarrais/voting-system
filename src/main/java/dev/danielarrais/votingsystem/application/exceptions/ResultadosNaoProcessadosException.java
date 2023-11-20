package dev.danielarrais.votingsystem.application.exceptions;

public class ResultadosNaoProcessadosException extends NegocioException {
    public ResultadosNaoProcessadosException(Long id) {
        super("A pauta de ID " + id + " ainda teve os resultados processados");
    }
}
