package dev.danielarrais.votingsystem.application.exceptions;

public class CPFNaoAutorizadoAVotarException extends NegocioException {
    public CPFNaoAutorizadoAVotarException(String CPF) {
        super("CPF " + CPF + "não está autorizado a votar");
    }
}
