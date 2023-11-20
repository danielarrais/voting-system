package dev.danielarrais.votingsystem.application.exceptions;

public class PautaComSessaoJaRegistradaException extends NegocioException {
    public PautaComSessaoJaRegistradaException(Long id) {
        super("A pauta de ID " + id + " já possui sessão de votação registrada");
    }
}
