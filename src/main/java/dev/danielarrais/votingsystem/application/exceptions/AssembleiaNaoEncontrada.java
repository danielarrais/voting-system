package dev.danielarrais.votingsystem.application.exceptions;

public class AssembleiaNaoEncontrada extends RuntimeException {
    public AssembleiaNaoEncontrada(Long id) {
        super("Assembleia de ID " + id + " não encontrada");
    }
}
