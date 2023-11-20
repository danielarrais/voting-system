package dev.danielarrais.votingsystem.application.exceptions;

public class ResultadosNaoProcessados extends NegocioException {
    public ResultadosNaoProcessados(Long id) {
        super("A pauta de ID " + id + " ainda teve os resultados processados");
    }
}
