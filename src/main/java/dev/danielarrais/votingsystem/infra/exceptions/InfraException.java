package dev.danielarrais.votingsystem.infra.exceptions;

public class InfraException extends RuntimeException{
    public InfraException(String message, Throwable cause) {
        super(message, cause);
    }
}
