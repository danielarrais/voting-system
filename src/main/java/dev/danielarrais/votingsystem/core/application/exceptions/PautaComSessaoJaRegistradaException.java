package dev.danielarrais.votingsystem.core.application.exceptions;

public class PautaComSessaoJaRegistradaException extends NegocioException {
    public PautaComSessaoJaRegistradaException(Long id) {
        super("A pauta de ID " + id + " já possui sessão de votação registrada");
    }
}
