package dev.danielarrais.votingsystem.infra.tasks;

import dev.danielarrais.votingsystem.core.application.service.in.ProcessarResultadoVotosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PublicarResultadosTask {
    private final ProcessarResultadoVotosUseCase processarResultadoVotosUseCase;

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void publicarResultados() {
        processarResultadoVotosUseCase.publicar();
    }
}
