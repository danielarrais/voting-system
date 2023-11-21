package dev.danielarrais.votingsystem.core.application.exceptions;

public class CpfNaoAutorizadoAVotarException extends NegocioException {
    public CpfNaoAutorizadoAVotarException(String CPF) {
        super("CPF " + CPF + "não está autorizado a votar");
    }
}
