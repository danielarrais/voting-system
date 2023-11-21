package dev.danielarrais.votingsystem.infra.broker;

import org.springframework.amqp.core.Message;
import org.springframework.context.annotation.Primary;

@Primary
public interface BrokerTemplate {
     void send(String routingKey, Message object);
}
