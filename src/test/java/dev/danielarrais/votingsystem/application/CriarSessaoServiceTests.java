package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.PautaComSessaoJaRegistradaException;
import dev.danielarrais.votingsystem.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.domain.Sessao;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CriarSessaoServiceTests {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoRepository sessaoRepository;

    @InjectMocks
    private CriarSessaoService criarSessaoService;

    @Test
    public void criar_falhaComPautaInvalida() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PautaNaoEncontradaException.class, () -> {
            criarSessaoService.criar(1L, generateValidSession());
        });

        Exception expected = new PautaNaoEncontradaException(1L);

        assertThat(exception)
                .extracting(Throwable::getMessage)
                .isEqualTo(expected.getMessage());
    }

    @Test
    public void criar_falhaQuandoAPautaJaTemSessao() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(new PautaEntity()));
        when(sessaoRepository.existsByPautaId(1L)).thenReturn(Boolean.TRUE);

        Exception exception = assertThrows(PautaComSessaoJaRegistradaException.class, () -> {
            criarSessaoService.criar(1L, generateValidSession());
        });

        Exception expected = new PautaComSessaoJaRegistradaException(1L);

        assertThat(exception)
                .extracting(Throwable::getMessage)
                .isEqualTo(expected.getMessage());
    }

    @Test
    public void criar_naoDarErro() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(new PautaEntity()));
        when(sessaoRepository.existsByPautaId(1L)).thenReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> {
            criarSessaoService.criar(1L, generateValidSession());
        });
    }

    private Sessao generateValidSession() {
        return Sessao.builder()
                .dataInicio(LocalDateTime.now())
                .duracao(1)
                .build();
    }

    //
}
