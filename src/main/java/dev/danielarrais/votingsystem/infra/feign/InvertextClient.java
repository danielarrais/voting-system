package dev.danielarrais.votingsystem.infra.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "invertext-com", url = "${invert-text.base-url}", configuration = InvertextConfiguration.class)
public interface InvertextClient {

    @GetMapping(path = "/validator")
    CpfValido cpfValido(@RequestParam(name = "value") String cpf);
}
