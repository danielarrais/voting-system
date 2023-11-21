package dev.danielarrais.votingsystem.infra.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class RabbitmqTemplateImpl implements BrokerTemplate {
    private final RabbitTemplate rabbitTemplate;

    public void send(String routingKey, Message object) {
        rabbitTemplate.send(routingKey, object);
    }
}
