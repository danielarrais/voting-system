package dev.danielarrais.votingsystem.infra.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "invertext-com", url = "${invert-text.base-url}", configuration = InvertextConfiguration.class)
public interface InvertextClient {

    @GetMapping(name = "/validator")
    CPFValido cpfValido(@PathVariable(name = "value") String CPF);
}
