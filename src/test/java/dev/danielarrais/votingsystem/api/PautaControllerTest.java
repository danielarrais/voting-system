package dev.danielarrais.votingsystem.api;

import dev.danielarrais.votingsystem.api.config.AbstractIT;
import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;


@Testcontainers
class PautaControllerTest extends AbstractIT {
    private static final String BASE_URL = "/v1/pautas";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private PautaRepository pautaRepository;

    @BeforeAll
    public static void configurar() {
        mySQLContainer.withReuse(true).start();
    }

    @AfterAll
    public static void parar() {
        mySQLContainer.stop();
    }

    @BeforeEach
    public void inserirDadosBasicos() {
        PautaEntity.PautaEntityBuilder pautaEntity = PautaEntity.builder()
                .titulo("Teste")
                .descricao("Teste");

        pautaRepository.save(pautaEntity.build());
        pautaRepository.save(pautaEntity.build());
        pautaRepository.save(pautaEntity.build());
        pautaRepository.save(pautaEntity.build());
        pautaRepository.save(pautaEntity.build());
    }

    @Test
    @Order(1)
    void buscarPautas_200_quandoBuscarTodasAsPautas() {
        var response = this.template.exchange(BASE_URL, GET, EMPTY, PautaResponse[].class);
        var jsonEsperado = PautasFactory.getRetornoOk();

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(OK);

        assertThat(response.getBody())
                .isNotNull()
                .hasSize(5)
                .isEqualTo(jsonEsperado);
    }

    @Test
    @Order(2)
    void buscarPautas_200_quandoNaoHouverPautas() {
        List<PautaEntity> pautasBackup = pautaRepository.findAll();
        pautaRepository.deleteAll();
        var response = this.template.exchange(BASE_URL, GET, EMPTY, PautaResponse[].class);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(OK);

        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();

        pautaRepository.saveAll(pautasBackup);
    }
}