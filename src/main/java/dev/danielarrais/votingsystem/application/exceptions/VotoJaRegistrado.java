package dev.danielarrais.votingsystem.application.exceptions;

public class VotoJaRegistrado extends NegocioException {
    public VotoJaRegistrado(String cpf) {
        super("Voto já registrado no CPF " + cpf);
    }
}
