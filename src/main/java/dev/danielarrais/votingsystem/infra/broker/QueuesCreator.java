package dev.danielarrais.votingsystem.infra.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueuesCreator {

    @Value("${broker.queues.queue-resultado-eleicao}")
    private String queueResultadoEleicao;

    @Bean
    public Queue simpleQueues() {
        return QueueBuilder.durable(queueResultadoEleicao).build();
    }
}
