package dev.danielarrais.votingsystem.application.exceptions;

public class PautaSemSessaoAbertaException extends NegocioException {
    public PautaSemSessaoAbertaException(Long id) {
        super("A pauta de ID " + id + " não possui sessão de votação registrada");
    }
}
