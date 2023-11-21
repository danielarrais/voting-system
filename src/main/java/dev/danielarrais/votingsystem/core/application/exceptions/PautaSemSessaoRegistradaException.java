package dev.danielarrais.votingsystem.core.application.exceptions;

public class PautaSemSessaoRegistradaException extends NegocioException {
    public PautaSemSessaoRegistradaException(Long id) {
        super("A pauta de ID " + id + " não possui sessão de votação registrada");
    }
}
