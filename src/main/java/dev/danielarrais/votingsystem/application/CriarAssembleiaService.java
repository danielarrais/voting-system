package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.infra.database.entities.AssembleiaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.AssembleiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarAssembleiaService {
    private final AssembleiaRepository assembleiaRepository;

    public void cria(LocalDateTime dataInicio, LocalDateTime dataEncerramento) {
        AssembleiaEntity assembleiaEntity = AssembleiaEntity.builder()
                .dataInicio(dataInicio)
                .dataEncerramento(dataEncerramento)
                .build();

        assembleiaRepository.save(assembleiaEntity);
    }
}
