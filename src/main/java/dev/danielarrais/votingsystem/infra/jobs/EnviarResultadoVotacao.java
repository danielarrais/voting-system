package dev.danielarrais.votingsystem.infra.jobs;

import dev.danielarrais.votingsystem.infra.broker.BrokerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnviarResultadoVotacao {
    private BrokerTemplate brokerTemplate;

    @Scheduled(fixedDelay = 1000)
    public void enviar() {

    }
}
