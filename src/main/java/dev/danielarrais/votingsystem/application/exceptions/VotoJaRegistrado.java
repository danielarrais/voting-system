package dev.danielarrais.votingsystem.application.exceptions;

public class VotoJaRegistrado extends RuntimeException {
    public VotoJaRegistrado(String cpf) {
        super("Voto já registrado no CPF " + cpf);
    }
}
