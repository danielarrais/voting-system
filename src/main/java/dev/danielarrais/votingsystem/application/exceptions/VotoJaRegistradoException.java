package dev.danielarrais.votingsystem.application.exceptions;

public class VotoJaRegistradoException extends NegocioException {
    public VotoJaRegistradoException(String cpf) {
        super("Voto já registrado no CPF " + cpf);
    }
}
