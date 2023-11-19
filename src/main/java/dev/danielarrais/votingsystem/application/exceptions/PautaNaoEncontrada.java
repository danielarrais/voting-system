package dev.danielarrais.votingsystem.application.exceptions;

public class PautaNaoEncontrada extends RuntimeException {
    public PautaNaoEncontrada(String message) {
        super(message);
    }
}
