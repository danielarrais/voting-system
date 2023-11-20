package dev.danielarrais.votingsystem.application.exceptions;

public class PautaComSessaoJaRegistrada extends NegocioException {
    public PautaComSessaoJaRegistrada(Long id) {
        super("A pauta de ID " + id + " já possui sessão de votação registrada");
    }
}
