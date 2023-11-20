package dev.danielarrais.votingsystem.application.exceptions;

public class PautaEmVotacao extends NegocioException {
    public PautaEmVotacao(Long id) {
        super("A pauta de ID " + id + " ainda está em votação");
    }
}
