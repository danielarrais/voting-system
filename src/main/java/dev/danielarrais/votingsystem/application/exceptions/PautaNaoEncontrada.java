package dev.danielarrais.votingsystem.application.exceptions;

public class PautaNaoEncontrada extends RuntimeException {
    public PautaNaoEncontrada(Long id) {
        super("Pauta de ID " + id + " não encontrada");
    }
}
