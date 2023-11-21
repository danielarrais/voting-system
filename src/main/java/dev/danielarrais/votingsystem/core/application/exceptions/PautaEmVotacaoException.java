package dev.danielarrais.votingsystem.core.application.exceptions;

public class PautaEmVotacaoException extends NegocioException {
    public PautaEmVotacaoException(Long id) {
        super("A pauta de ID " + id + " ainda está em votação");
    }
}
