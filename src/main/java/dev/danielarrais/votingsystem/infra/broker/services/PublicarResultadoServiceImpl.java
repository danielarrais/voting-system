package dev.danielarrais.votingsystem.infra.broker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.danielarrais.votingsystem.core.application.service.out.PublicarResultadoService;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.infra.broker.BrokerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublicarResultadoServiceImpl implements PublicarResultadoService {
    private final BrokerTemplate brokerTemplate;
    private final ObjectMapper objectMapper;

    @Value("${voting.broker.queues.queue-resultado-eleicao}")
    private String resultadoQueue;

    @Override
    public void publicar(Resultado resultado) {
        try {
            var json = objectMapper.writeValueAsString(resultado);
            brokerTemplate.send(resultadoQueue, new Message(json.getBytes()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
