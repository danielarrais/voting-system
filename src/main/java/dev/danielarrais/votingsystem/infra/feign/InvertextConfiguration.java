package dev.danielarrais.votingsystem.infra.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class InvertextConfiguration {

    @Value("${invert-text.token}")
    String token;

    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return requestTemplate -> requestTemplate
                .query("token", token);
    }
}
