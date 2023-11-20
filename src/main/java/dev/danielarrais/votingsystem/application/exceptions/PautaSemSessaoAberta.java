package dev.danielarrais.votingsystem.application.exceptions;

public class PautaSemSessaoAberta extends NegocioException {
    public PautaSemSessaoAberta(Long id) {
        super("A pauta de ID " + id + " não possui sessão de votação registrada");
    }
}
