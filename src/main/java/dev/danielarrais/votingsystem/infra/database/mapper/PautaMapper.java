package dev.danielarrais.votingsystem.infra.database.mapper;

import dev.danielarrais.votingsystem.core.domain.Pauta;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PautaMapper {
    public PautaEntity convert(Pauta pauta) {
        return PautaEntity.builder()
                .titulo(pauta.getTitulo())
                .descricao(pauta.getDescricao())
                .build();
    }

    public Pauta convert(PautaEntity pautaEntity) {
        return Pauta.builder()
                .id(pautaEntity.getId())
                .titulo(pautaEntity.getTitulo())
                .descricao(pautaEntity.getDescricao())
                .build();
    }

    public List<Pauta> convert(List<PautaEntity> pautasEntities) {
        return pautasEntities.stream()
                .map(PautaMapper::convert)
                .collect(Collectors.toList());
    }
}
