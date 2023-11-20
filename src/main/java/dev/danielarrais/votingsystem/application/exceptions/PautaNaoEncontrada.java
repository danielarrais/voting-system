package dev.danielarrais.votingsystem.application.exceptions;

public class PautaNaoEncontrada extends NegocioException {
    public PautaNaoEncontrada(Long id) {
        super("Pauta de ID " + id + " não encontrada");
    }
}
