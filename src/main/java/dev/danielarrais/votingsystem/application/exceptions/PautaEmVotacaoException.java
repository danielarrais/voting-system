package dev.danielarrais.votingsystem.application.exceptions;

public class PautaEmVotacaoException extends NegocioException {
    public PautaEmVotacaoException(Long id) {
        super("A pauta de ID " + id + " ainda está em votação");
    }
}
