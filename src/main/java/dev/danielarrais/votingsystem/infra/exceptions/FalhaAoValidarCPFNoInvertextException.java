package dev.danielarrais.votingsystem.infra.exceptions;

public class FalhaAoValidarCPFNoInvertextException extends InfraException {
    public FalhaAoValidarCPFNoInvertextException(Throwable cause) {
        super("Falha ao se comunicar com o invertext.com", cause);
    }
}
