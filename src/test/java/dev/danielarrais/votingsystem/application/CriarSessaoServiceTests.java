package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.core.application.exceptions.PautaComSessaoJaRegistradaException;
import dev.danielarrais.votingsystem.core.application.service.in.impl.CriarSessaoUserCaseImpl;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarSessaoService;
import dev.danielarrais.votingsystem.core.application.service.out.RegistraSessaoService;
import dev.danielarrais.votingsystem.core.domain.Sessao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CriarSessaoServiceTests {

    @Mock
    private RegistraSessaoService registraSessaoService;

    @Mock
    private RecuperarSessaoService recuperarSessaoService;

    @InjectMocks
    private CriarSessaoUserCaseImpl criarSessaoUserCase;


    @Test
    public void criar_falhaQuandoAPautaJaTemSessao() {
        when(recuperarSessaoService.existePautaPeloId(1L)).thenReturn(Boolean.TRUE);

        Exception exception = assertThrows(PautaComSessaoJaRegistradaException.class, () -> {
            criarSessaoUserCase.criar(1L, generateValidSession());
        });

        Exception expected = new PautaComSessaoJaRegistradaException(1L);

        assertThat(exception)
                .extracting(Throwable::getMessage)
                .isEqualTo(expected.getMessage());
    }

    @Test
    public void criar_naoDarErro() {
        when(recuperarSessaoService.existePautaPeloId(1L)).thenReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> {
            criarSessaoUserCase.criar(1L, generateValidSession());
        });
    }

    private Sessao generateValidSession() {
        return Sessao.builder()
                .dataInicio(LocalDateTime.now())
                .duracao(1)
                .build();
    }
}
