package dev.danielarrais.votingsystem.infra.database.mapper;

import dev.danielarrais.votingsystem.core.domain.Pauta;
import dev.danielarrais.votingsystem.core.domain.Sessao;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.SessaoEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessaoMapper {
    public static SessaoEntity convert(Sessao sessao, PautaEntity pautaEntity) {
        return SessaoEntity.builder()
                .dataInicio(sessao.getDataInicio())
                .dataEncerramento(sessao.getDataEncerramento())
                .pauta(pautaEntity)
                .build();
    }
}
