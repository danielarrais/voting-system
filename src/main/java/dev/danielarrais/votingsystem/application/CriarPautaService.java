package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarPautaService {
    private final PautaRepository pautaRepository;

    public void cria(String titulo, String descricao) {
        PautaEntity pautaEntity = PautaEntity.builder()
                .titulo(titulo)
                .descricao(descricao)
                .build();

        pautaRepository.save(pautaEntity);
    }
}
