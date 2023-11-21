package dev.danielarrais.votingsystem.api;

import dev.danielarrais.votingsystem.api.config.AbstractIT;
import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

class PautaControllerTest extends AbstractIT {
    private static final String BASE_URL = "/v1/pautas";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    @Order(1)
    void buscarPautas_200_quandoBuscarTodasAsPautas() {
        var response = this.template.exchange(BASE_URL, GET, EMPTY, new ParameterizedTypeReference<ArrayList<PautaResponse>>() {
        });
        var jsonEsperado = PautasFactory.getRetornoOk();

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(OK);

        assertThat(response.getBody())
                .isNotNull().asList()
                .hasSize(5)
                .isEqualTo(jsonEsperado);
    }

    @Test
    @Order(2)
    void buscarPautas_200_quandoNaoHouverPautas() {
        List<PautaEntity> pautasBackup = pautaRepository.findAll();
        pautaRepository.deleteAll();
        var response = this.template.exchange(BASE_URL, GET, EMPTY, new ParameterizedTypeReference<ArrayList<PautaResponse>>() {
        });

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(OK);

        assertThat(response.getBody())
                .isNotNull().asList()
                .isEmpty();

        pautaRepository.saveAll(pautasBackup);
    }
}