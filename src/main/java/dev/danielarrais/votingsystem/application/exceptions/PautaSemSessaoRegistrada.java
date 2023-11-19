package dev.danielarrais.votingsystem.application.exceptions;

public class PautaSemSessaoRegistrada extends RuntimeException {
    public PautaSemSessaoRegistrada(Long id) {
        super("A pauta de ID " + id + " não possui sessão de votação registrada");
    }
}
