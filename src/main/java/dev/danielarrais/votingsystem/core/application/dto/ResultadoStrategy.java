package dev.danielarrais.votingsystem.core.application.dto;

public interface ResultadoStrategy {
    ResultadoEnum aplicarRegra(Integer votosFavoraveis, Integer votosContra);
}
