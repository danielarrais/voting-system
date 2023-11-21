package dev.danielarrais.votingsystem.core.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ResultadoEnum implements ResultadoStrategy {
    APROVADA {
        @Override
        public ResultadoEnum aplicarRegra(Integer votosFavoraveis, Integer votosContra) {
            return votosFavoraveis > votosContra ? APROVADA : null;
        }
    },
    EMPATADA {
        @Override
        public ResultadoEnum aplicarRegra(Integer votosFavoraveis, Integer votosContra) {
            return votosFavoraveis.equals(votosContra) && votosFavoraveis != 0 ? EMPATADA : null;
        }
    },
    ABSTENCAO {
        @Override
        public ResultadoEnum aplicarRegra(Integer votosFavoraveis, Integer votosContra) {
            return votosFavoraveis.equals(0) && votosContra.equals(0) ? ABSTENCAO : null;
        }
    },
    REPROVADA {
        @Override
        public ResultadoEnum aplicarRegra(Integer votosFavoraveis, Integer votosContra) {
            return votosFavoraveis < votosContra ? REPROVADA : null;
        }
    };

    public static ResultadoEnum getResult(Integer left, Integer right) {
        return Arrays.stream(values())
                .filter(resultadoEnum -> resultadoEnum.aplicarRegra(left, right) != null)
                .findFirst().orElse(REPROVADA);
    }
}
