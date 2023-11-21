package dev.danielarrais.votingsystem.core.application.exceptions;

public class PautaSemSessaoAbertaException extends NegocioException {
    public PautaSemSessaoAbertaException(Long id) {
        super("A pauta de ID " + id + " não possui sessão de votação aberta");
    }
}
