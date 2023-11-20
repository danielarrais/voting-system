package dev.danielarrais.votingsystem.application.exceptions;

public class CPFNaoAutorizadoAVotar extends NegocioException {
    public CPFNaoAutorizadoAVotar(String CPF) {
        super("CPF " + CPF + "não está autorizado a votar");
    }
}
