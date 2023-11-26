package dev.danielarrais.votingsystem.api;

import dev.danielarrais.votingsystem.api.config.AbstractIT;
import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.api.dto.response.ErroResponse;
import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;
import dev.danielarrais.votingsystem.core.application.dto.ResultadoEnum;
import dev.danielarrais.votingsystem.core.application.exceptions.CpfNaoAutorizadoAVotarException;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaEmVotacaoException;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaSemSessaoAbertaException;
import dev.danielarrais.votingsystem.core.application.exceptions.VotoJaRegistradoException;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
import dev.danielarrais.votingsystem.infra.feign.service.ValidaCpfServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Testcontainers
class PautaControllerTest extends AbstractIT {
    private static final Integer DURACAO_PADRAO_SESSAO = 1;
    private static final String PAUTAS_URL = "/v1/pautas";
    private static final String CRIAR_SESSOES_URL = PAUTAS_URL + "/%d/sessoes";
    private static final String CRIAR_SESSOES_COM_DURACAO_URL = PAUTAS_URL + "/%d/sessoes?duracao=%d";
    private static final String VOTAR_URL = PAUTAS_URL + "/%d/votos";
    private static final String RESULTADOS_URL = PAUTAS_URL + "/%d/resultados";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @MockBean
    private ValidaCpfServiceImpl validaCpfService;

    private List<PautaEntity> pautasInseridas;

    @BeforeEach
    public void configurarDados() {
        inserirPautas();
    }

    @AfterEach
    public void removerDados() {
        removerResultados();
        removerVotos();
        removerSessoes();
        removerPautas();
    }

    @Test
    void buscarPautas_200_quandoBuscarTodasAsPautas() {
        var response = this.template.getForEntity(PAUTAS_URL, PautaResponse[].class);
        var jsonEsperado = PautasFactory.getRetornoOk();

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(OK);

        assertThat(response.getBody())
                .isNotNull()
                .hasSize(5)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(jsonEsperado);
    }

    @Test
    void buscarPautas_200_quandoNaoHouverPautas() {
        pautaRepository.deleteAll();
        var response = this.template.getForEntity(PAUTAS_URL, PautaResponse[].class);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(OK);

        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void criarPauta_201_quandoCriaCorretante() {
        var pautaRequest = PautaRequest.builder().titulo("Teste").descricao("Teste").build();
        var response = this.template.postForEntity(PAUTAS_URL, pautaRequest, String.class);
        var pautasRegistradas = pautaRepository.count();

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(CREATED);

        assertThat(pautasRegistradas)
                .isEqualTo(pautasInseridas.size() + 1);

    }

    @Test
    void criarPauta_404_quandoNaoEnviaOTitulo() {
        var pautaRequest = PautaRequest.builder().descricao("Teste").build();
        var response = this.template.postForEntity(PAUTAS_URL, pautaRequest, String.class);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(BAD_REQUEST);
    }

    @Test
    void criarPauta_400_quandoNaoEnviaADescricao() {
        var pautaRequest = PautaRequest.builder().titulo("Teste").build();
        var response = this.template.postForEntity(PAUTAS_URL, pautaRequest, String.class);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(BAD_REQUEST);
    }

    @Test
    void criarSessao_201_quandoCriaSessaoCorretante() {
        var idPauta = getIdDePautaValido();
        var url = String.format(CRIAR_SESSOES_URL, idPauta);
        var response = this.template.postForEntity(url, null, String.class);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(CREATED);
    }

    @Test
    void criarSessao_201_comDuracaoPadraoDeUmMinuto() {
        var idPauta = getIdDePautaValido();
        var url = String.format(CRIAR_SESSOES_URL, idPauta);
        var response = this.template.postForEntity(url, null, String.class);
        var sessaoInserida = this.sessaoRepository.findByPautaId(idPauta);
        var dataDeEncerramento = sessaoInserida.get().getDataEncerramento();
        var dataDeInicio = sessaoInserida.get().getDataInicio();

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(CREATED);

        assertThat(dataDeInicio)
                .isEqualTo(dataDeEncerramento.minusMinutes(DURACAO_PADRAO_SESSAO));
    }

    @Test
    void criarSessao_201_comDuracaoDiferenteDeUmMinuto() {
        var duracaoPauta = 30;
        var idPauta = getIdDePautaValido();
        var url = String.format(CRIAR_SESSOES_COM_DURACAO_URL, idPauta, duracaoPauta);
        var response = this.template.postForEntity(url, null, String.class);
        var sessaoInserida = this.sessaoRepository.findByPautaId(idPauta);
        var dataDeEncerramento = sessaoInserida.get().getDataEncerramento();
        var dataDeInicio = sessaoInserida.get().getDataInicio();


        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(CREATED);

        assertThat(dataDeInicio)
                .isEqualTo(dataDeEncerramento.minusMinutes(duracaoPauta));
    }

    @Test
    void criarSessao_404_quandoAPautaNaoExiste() {
        var idPautaInvalido = getIdDePautaInvalido();
        var response = criarSessao(idPautaInvalido);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(NOT_FOUND);
    }

    @Test
    void votar_201_quandoAPautaExiste() {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var voto = VotoRequest.builder().voto(true).cpf("06412721380").build();
        var idPautaValido = getIdDePautaValido();

        criarSessao(idPautaValido);

        var url = String.format(VOTAR_URL, idPautaValido);
        var response = this.template.postForEntity(url, voto, String.class);

        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(CREATED);
    }

    @Test
    void votar_400_quandoASessaoNaoExiste() {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var voto = VotoRequest.builder().voto(true).cpf("06412721380").build();
        var idPautaValido = getIdDePautaValido();
        var url = String.format(VOTAR_URL, idPautaValido);
        var response = this.template.postForEntity(url, voto, ErroResponse.class).getBody();
        var mensagemEsperada = new PautaSemSessaoAbertaException(idPautaValido).getMessage();

        assertThat(response)
                .isNotNull()
                .extracting(ErroResponse::getMensagem, ErroResponse::getCodigo)
                .contains(mensagemEsperada, BAD_REQUEST.value());
    }

    @Test
    void votar_400_quandoOCPFInvalido() {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.FALSE);
        var idPautaValido = getIdDePautaValido();
        var voto = VotoRequest.builder().voto(true).cpf("4234234234").build();
        var mensagemEsperada = new CpfNaoAutorizadoAVotarException(voto.getCpf()).getMessage();

        var url = String.format(VOTAR_URL, idPautaValido);
        var response = this.template.postForEntity(url, voto, ErroResponse.class).getBody();

        assertThat(response)
                .isNotNull()
                .extracting(ErroResponse::getMensagem, ErroResponse::getCodigo)
                .contains(mensagemEsperada, BAD_REQUEST.value());
    }

    @Test
    void votar_400_quandoASessaoEstarFechada() throws InterruptedException {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idPautaValido = getIdDePautaValido();
        var voto = VotoRequest.builder().voto(true).cpf("4234234234").build();
        var mensagemEsperada = new PautaSemSessaoAbertaException(idPautaValido).getMessage();

        criarSessao(idPautaValido);
        esperarSessaoEncerrar();

        var url = String.format(VOTAR_URL, idPautaValido);
        var response = this.template.postForEntity(url, voto, ErroResponse.class).getBody();

        assertThat(response)
                .isNotNull()
                .extracting(ErroResponse::getMensagem, ErroResponse::getCodigo)
                .contains(mensagemEsperada, BAD_REQUEST.value());
    }

    @Test
    void votar_400_quandoJaVotou() {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idPautaValido = getIdDePautaValido();
        var voto = VotoRequest.builder().voto(true).cpf("4234234234").build();
        var mensagemEsperada = new VotoJaRegistradoException(voto.getCpf()).getMessage();

        criarSessao(idPautaValido);

        var url = String.format(VOTAR_URL, idPautaValido);
        this.template.postForEntity(url, voto, String.class);
        var responseErro = this.template.postForEntity(url, voto, ErroResponse.class).getBody();

        assertThat(responseErro)
                .isNotNull()
                .extracting(ErroResponse::getMensagem, ErroResponse::getCodigo)
                .contains(mensagemEsperada, BAD_REQUEST.value());
    }

    @Test
    void resultados_200_resultadoCorretoQuandoTemMaisVotosFavoraveis() throws InterruptedException {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idPautaValido = getIdDePautaValido();
        var url = String.format(RESULTADOS_URL, idPautaValido);

        criarSessao(idPautaValido);
        var resultadoEsperado = criarResultadoFavoravel(idPautaValido);
        esperarSessaoEncerrar();
        var resultado = this.template.getForEntity(url, Resultado.class);

        assertThat(resultadoEsperado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(resultado.getBody());
    }

    @Test
    void resultados_200_resultadoCorretoQuandoTemMaisVotosDesfavoraveis() throws InterruptedException {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idPautaValido = getIdDePautaValido();
        var url = String.format(RESULTADOS_URL, idPautaValido);

        criarSessao(idPautaValido);
        var resultadoEsperado = criarResultadoDesfavoravel(idPautaValido);
        esperarSessaoEncerrar();
        var resultado = this.template.getForEntity(url, Resultado.class);

        assertThat(resultadoEsperado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(resultado.getBody());
    }

    @Test
    void resultados_200_resultadoCorretoQuandoDeAbstencao() throws InterruptedException {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idPautaValido = getIdDePautaValido();
        var url = String.format(RESULTADOS_URL, idPautaValido);

        criarSessao(idPautaValido);
        var resultadoEsperado = criarResultadoAbstencao(idPautaValido);
        esperarSessaoEncerrar();
        var resultado = this.template.getForEntity(url, Resultado.class);

        assertThat(resultadoEsperado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(resultado.getBody());
    }

    @Test
    void resultados_200_resultadoCorretoQuandoDeEmpate() throws InterruptedException {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idPautaValido = getIdDePautaValido();
        var url = String.format(RESULTADOS_URL, idPautaValido);

        criarSessao(idPautaValido);
        var resultadoEsperado = criarResultadoEmpate(idPautaValido);
        esperarSessaoEncerrar();
        var resultado = this.template.getForEntity(url, Resultado.class);

        assertThat(resultadoEsperado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(resultado.getBody());
    }

    @Test
    void resultados_404_quandoAPautaNaoExiste() {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idDePautaInvalido = getIdDePautaInvalido();
        var url = String.format(RESULTADOS_URL, idDePautaInvalido);
        var resultado = this.template.getForEntity(url, String.class);

        assertThat(resultado)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(NOT_FOUND);
    }

    @Test
    void resultados_400_quandoSessaoNaoEncerrou() {
        Mockito.when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        var idDePautaValido = getIdDePautaValido();
        var url = String.format(RESULTADOS_URL, idDePautaValido);

        criarSessao(idDePautaValido);

        var resultado = this.template.getForEntity(url, ErroResponse.class);
        var mensagemEsperada = new PautaEmVotacaoException(idDePautaValido).getMessage();

        assertThat(resultado.getBody())
                .isNotNull()
                .extracting(ErroResponse::getMensagem, ErroResponse::getCodigo)
                .contains(mensagemEsperada, BAD_REQUEST.value());
    }

    public void inserirPautas() {
        pautasInseridas = new ArrayList<>();
        PautaEntity.PautaEntityBuilder pautaEntity = PautaEntity.builder()
                .titulo("Teste")
                .descricao("Teste");

        pautasInseridas.add(pautaRepository.save(pautaEntity.build()));
        pautasInseridas.add(pautaRepository.save(pautaEntity.build()));
        pautasInseridas.add(pautaRepository.save(pautaEntity.build()));
        pautasInseridas.add(pautaRepository.save(pautaEntity.build()));
        pautasInseridas.add(pautaRepository.save(pautaEntity.build()));
    }

    private ResponseEntity<String> criarSessao(Long idPauta) {
        var urlCriarSessao = String.format(CRIAR_SESSOES_URL, idPauta);
        return this.template.postForEntity(urlCriarSessao, null, String.class);
    }

    private Resultado criarResultadoFavoravel(Long idPauta) {
        var voto1 = VotoRequest.builder().voto(true).cpf("4234234234").build();
        var voto2 = VotoRequest.builder().voto(true).cpf("4234234235").build();
        var voto3 = VotoRequest.builder().voto(false).cpf("4234234236").build();

        var url = String.format(VOTAR_URL, idPauta);
        this.template.postForEntity(url, voto1, String.class);;
        this.template.postForEntity(url, voto2, String.class);;
        this.template.postForEntity(url, voto3, String.class);;

        return Resultado.builder()
                .resultado(ResultadoEnum.APROVADA.name())
                .votosFavoraveis(2)
                .votosContrarios(1)
                .pautaId(idPauta)
                .build();
    }

    private Resultado criarResultadoDesfavoravel(Long idPauta) {
        var voto1 = VotoRequest.builder().voto(true).cpf("4234234234").build();
        var voto2 = VotoRequest.builder().voto(false).cpf("4234234235").build();
        var voto3 = VotoRequest.builder().voto(false).cpf("4234234236").build();

        var url = String.format(VOTAR_URL, idPauta);
        this.template.postForEntity(url, voto1, String.class);;
        this.template.postForEntity(url, voto2, String.class);;
        this.template.postForEntity(url, voto3, String.class);;

        return Resultado.builder()
                .resultado(ResultadoEnum.REPROVADA.name())
                .votosFavoraveis(1)
                .votosContrarios(2)
                .pautaId(idPauta)
                .build();
    }

    private Resultado criarResultadoEmpate(Long idPauta) {
        var voto1 = VotoRequest.builder().voto(true).cpf("4234234234").build();
        var voto2 = VotoRequest.builder().voto(false).cpf("4234234235").build();

        var url = String.format(VOTAR_URL, idPauta);
        this.template.postForEntity(url, voto1, String.class);;
        this.template.postForEntity(url, voto2, String.class);;

        return Resultado.builder()
                .resultado(ResultadoEnum.EMPATADA.name())
                .votosFavoraveis(1)
                .votosContrarios(1)
                .pautaId(idPauta)
                .build();
    }

    private Resultado criarResultadoAbstencao(Long idPauta) {
        return Resultado.builder()
                .resultado(ResultadoEnum.ABSTENCAO.name())
                .votosFavoraveis(0)
                .votosContrarios(0)
                .pautaId(idPauta)
                .build();
    }

    private void removerPautas() {
        pautaRepository.deleteAll();
    }


    private void esperarSessaoEncerrar() throws InterruptedException {
        Thread.sleep(61 * 1000); // 1 minuto
    }

    private void removerSessoes() {
        sessaoRepository.deleteAll();
    }

    private void removerVotos() {
        votoRepository.deleteAll();
    }

    private void removerResultados() {
        resultadoRepository.deleteAll();
    }

    private Long getIdDePautaInvalido() {
        return pautasInseridas.stream().max(Comparator.comparing(PautaEntity::getId)).get().getId() + 1L;
    }

    private Long getIdDePautaValido() {
        return pautasInseridas.stream().findAny().get().getId();
    }
}