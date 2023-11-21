package dev.danielarrais.votingsystem.infra.database.mapper;

import dev.danielarrais.votingsystem.core.domain.Voto;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.VotoEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class VotoMapper {
    public VotoEntity convert(PautaEntity pautaEntity, Voto voto) {
        return VotoEntity.builder()
                .pauta(pautaEntity)
                .voto(voto.getVoto())
                .cpf(voto.getCpf())
                .hora(voto.getHora())
                .build();
    }

    public Voto convert(VotoEntity votoEntity) {
        return Voto.builder()
                .pautaId(votoEntity.getPauta().getId())
                .voto(votoEntity.getVoto())
                .cpf(votoEntity.getCpf())
                .hora(votoEntity.getHora())
                .build();
    }

    public List<Voto> convert(List<VotoEntity> votosEntities) {
        return votosEntities.stream()
                .map(VotoMapper::convert)
                .collect(Collectors.toList());
    }
}
