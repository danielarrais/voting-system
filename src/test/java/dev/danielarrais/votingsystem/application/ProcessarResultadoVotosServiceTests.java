package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.dto.ResultadoEnum;
import dev.danielarrais.votingsystem.application.exceptions.PautaEmVotacaoException;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.ResultadoEntity;
import dev.danielarrais.votingsystem.infra.database.entities.VotoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
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
public class ProcessarResultadoVotosServiceTests {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private ProcessarResultadoVotosServiceImpl processarResultadoVotosService;

    @Test
    public void resultado_falhaComPautaAindaEmVotacao() {
        Mockito.when(sessaoRepository.sessaoPautaEstarAberta(1L)).thenReturn(Boolean.TRUE);

        var exception = assertThrows(PautaEmVotacaoException.class, () -> {
            processarResultadoVotosService.resultado(1L);
        });

        var expected = new PautaEmVotacaoException(1L);

        assertThat(exception)
                .extracting(Throwable::getMessage)
                .isEqualTo(expected.getMessage());

        verify(sessaoRepository, times(1)).sessaoPautaEstarAberta(any());
        verify(votoRepository, never()).findByPautaId(any());
        verify(pautaRepository, never()).findLockById(any());
        verify(resultadoRepository, never()).save(any());
    }

    @Test
    public void resultado_naoFalhaQuandoASessaoEstarFechada() {
        Mockito.when(sessaoRepository.sessaoPautaEstarAberta(1L)).thenReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> {
            processarResultadoVotosService.resultado(1L);
        });

        verify(sessaoRepository, times(1)).sessaoPautaEstarAberta(any());
        verify(votoRepository, times(1)).findByPautaId(any());
        verify(pautaRepository, times(1)).findLockById(any());
        verify(resultadoRepository, times(1)).save(any());
    }

    @Test
    public void processarVotos_retornaResultadoDeAprovacaoCorretamente() {
        var votos = geraVotosComMaisFavoraveis();
        var pauta = gerarPauta();
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta);
        var esperado = ResultadoEntity.builder()
                .votosFavoraveis(2)
                .votosContrarios(1)
                .resultado(ResultadoEnum.APROVADA.name())
                .pauta(pauta)
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
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta);
        var esperado = ResultadoEntity.builder()
                .votosFavoraveis(1)
                .votosContrarios(1)
                .resultado(ResultadoEnum.EMPATADA.name())
                .pauta(pauta)
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
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta);
        var esperado = ResultadoEntity.builder()
                .votosFavoraveis(0)
                .votosContrarios(0)
                .resultado(ResultadoEnum.ABSTENCAO.name())
                .pauta(pauta)
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
        var resultado = processarResultadoVotosService.processarVotos(votos, pauta);
        var esperado = ResultadoEntity.builder()
                .votosFavoraveis(1)
                .votosContrarios(2)
                .resultado(ResultadoEnum.REPROVADA.name())
                .pauta(pauta)
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

    public List<VotoEntity> geraVotosComMaisFavoraveis() {
        return Arrays.asList(
                VotoEntity.builder()
                        .cpf("TESTE")
                        .voto(true)
                        .build(),
                VotoEntity.builder()
                        .cpf("TESTE2")
                        .voto(true)
                        .build(),
                VotoEntity.builder()
                        .cpf("TESTE3")
                        .voto(false)
                        .build()
        );
    }

    public List<VotoEntity> geraVotosReprovacao() {
        return Arrays.asList(
                VotoEntity.builder()
                        .cpf("TESTE")
                        .voto(true)
                        .build(),
                VotoEntity.builder()
                        .cpf("TESTE2")
                        .voto(false)
                        .build(),
                VotoEntity.builder()
                        .cpf("TESTE3")
                        .voto(false)
                        .build()
        );
    }

    public List<VotoEntity> geraVotosEmpatados() {
        return Arrays.asList(
                VotoEntity.builder()
                        .cpf("TESTE")
                        .voto(true)
                        .build(),
                VotoEntity.builder()
                        .cpf("TESTE3")
                        .voto(false)
                        .build()
        );
    }

    public List<VotoEntity> geraVotosAbstencao() {
        return Collections.emptyList();
    }
}
