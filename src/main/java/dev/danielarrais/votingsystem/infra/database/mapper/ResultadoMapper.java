package dev.danielarrais.votingsystem.infra.database.mapper;

import dev.danielarrais.votingsystem.core.domain.Pauta;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.ResultadoEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResultadoMapper {
    public ResultadoEntity convert(Resultado resultado) {
        return ResultadoEntity.builder()
                .resultado(resultado.getResultado())
                .votosFavoraveis(resultado.getVotosFavoraveis())
                .votosContrarios(resultado.getVotosContrarios())
                .build();
    }
}
