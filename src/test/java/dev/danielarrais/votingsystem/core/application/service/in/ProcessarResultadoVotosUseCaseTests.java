package dev.danielarrais.votingsystem.core.application.service.in;

import dev.danielarrais.votingsystem.core.application.dto.ResultadoEnum;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaEmVotacaoException;
import dev.danielarrais.votingsystem.core.application.service.in.impl.ProcessarResultadoVotosUseCaseImpl;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarPautaService;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarSessaoService;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarVotosService;
import dev.danielarrais.votingsystem.core.application.service.out.SalvarResultadoService;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.core.domain.Voto;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProcessarResultadoVotosUseCaseTests {

    @Mock
    private RecuperarPautaService pautaRepository;

    @Mock
    private RecuperarSessaoService sessaoRepository;

    @Mock
    private RecuperarVotosService votoRepository;

    @Mock
    private SalvarResultadoService resultadoRepository;

    @InjectMocks
    private ProcessarResultadoVotosUseCaseImpl processarResultadoVotosService;

    @Test
    public void resultado_falhaComPautaAindaEmVotacao() {
        Mockito.when(sessaoRepository.sessaoDaPautaEstarAberta(1L)).thenReturn(Boolean.TRUE);

        var exception = assertThrows(PautaEmVotacaoException.class, () -> {
            processarResultadoVotosService.processarResultado(1L);
        });

        var expected = new PautaEmVotacaoException(1L);

        assertThat(exception)
                .extracting(Throwable::getMessage)
                .isEqualTo(expected.getMessage());

        verify(sessaoRepository, times(1)).sessaoDaPautaEstarAberta(any());
        verify(votoRepository, never()).buscaVotosDaPauta(any());
        verify(resultadoRepository, never()).salvar(any());
    }

    @Test
    public void resultado_naoFalhaQuandoASessaoEstarFechada() {
        Mockito.when(sessaoRepository.sessaoDaPautaEstarAberta(1L)).thenReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> {
            processarResultadoVotosService.processarResultado(1L);
        });

        verify(sessaoRepository, times(1)).sessaoDaPautaEstarAberta(any());
        verify(votoRepository, times(1)).buscaVotosDaPauta(any());
        verify(resultadoRepository, times(1)).salvar(any());
    }

    @Test
    public void processarVotos_retornaResultadoDeAprovacaoCorretamente() {
        var votos = geraVotosComMaisFavoraveis();
        var pauta = gerarPauta();
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta.getId());
        var esperado = Resultado.builder()
                .votosFavoraveis(2)
                .votosContrarios(1)
                .resultado(ResultadoEnum.APROVADA.name())
                .pautaId(pauta.getId())
                .build();

        assertThat(resultado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(esperado);
    }

    @Test
    public void processarVotos_retornaResultadoDeEmpateCorretamente() {
        var votos = geraVotosEmpatados();
        var pauta = gerarPauta();
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta.getId());
        var esperado = Resultado.builder()
                .votosFavoraveis(1)
                .votosContrarios(1)
                .resultado(ResultadoEnum.EMPATADA.name())
                .pautaId(pauta.getId())
                .build();

        assertThat(resultado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(esperado);
    }

    @Test
    public void processarVotos_retornaResultadoAbstencaoCorretamente() {
        var votos = geraVotosAbstencao();
        var pauta = gerarPauta();
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta.getId());
        var esperado = Resultado.builder()
                .votosFavoraveis(0)
                .votosContrarios(0)
                .resultado(ResultadoEnum.ABSTENCAO.name())
                .pautaId(pauta.getId())
                .build();

        assertThat(resultado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(esperado);
    }

    @Test
    public void processarVotos_retornaResultadoReprovacaoCorretamente() {
        var votos = geraVotosReprovacao();
        var pauta = gerarPauta();
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta.getId());
        var esperado = Resultado.builder()
                .votosFavoraveis(1)
                .votosContrarios(2)
                .resultado(ResultadoEnum.REPROVADA.name())
                .pautaId(pauta.getId())
                .build();

        assertThat(resultado)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(esperado);
    }

    public PautaEntity gerarPauta() {
        return PautaEntity.builder()
                .id(1L)
                .titulo("Teste")
                .descricao("Teste")
                .build();
    }

    public List<Voto> geraVotosComMaisFavoraveis() {
        return Arrays.asList(
                Voto.builder()
                        .cpf("TESTE")
                        .voto(true)
                        .build(),
                Voto.builder()
                        .cpf("TESTE2")
                        .voto(true)
                        .build(),
                Voto.builder()
                        .cpf("TESTE3")
                        .voto(false)
                        .build()
        );
    }

    public List<Voto> geraVotosReprovacao() {
        return Arrays.asList(
                Voto.builder()
                        .cpf("TESTE")
                        .voto(true)
                        .build(),
                Voto.builder()
                        .cpf("TESTE2")
                        .voto(false)
                        .build(),
                Voto.builder()
                        .cpf("TESTE3")
                        .voto(false)
                        .build()
        );
    }

    public List<Voto> geraVotosEmpatados() {
        return Arrays.asList(
                Voto.builder()
                        .cpf("TESTE")
                        .voto(true)
                        .build(),
                Voto.builder()
                        .cpf("TESTE3")
                        .voto(false)
                        .build()
        );
    }

    public List<Voto> geraVotosAbstencao() {
        return Collections.emptyList();
    }
}
