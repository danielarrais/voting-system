package dev.danielarrais.votingsystem.application.exceptions;

public class PautaNaoEncontradaException extends NegocioException {
    public PautaNaoEncontradaException(Long id) {
        super("Pauta de ID " + id + " não encontrada");
    }
}
