package dev.danielarrais.votingsystem.core.application.service.in;

import dev.danielarrais.votingsystem.core.application.exceptions.CpfNaoAutorizadoAVotarException;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaSemSessaoAbertaException;
import dev.danielarrais.votingsystem.core.application.exceptions.VotoJaRegistradoException;
import dev.danielarrais.votingsystem.core.application.service.in.impl.RegistrarVotoUserCaseImpl;
import dev.danielarrais.votingsystem.core.application.service.out.*;
import dev.danielarrais.votingsystem.core.domain.Voto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegistrarVotoUseCaseTests {

    private final String CPF_VALIDO = "064127324234";
    private final String CPF_INVALIDO = "345345345345";

    @Mock
    private RecuperarPautaService recuperarPautaService;

    @Mock
    private ValidaCpfService validaCpfService;

    @Mock
    private RecuperarSessaoService recuperarSessaoService;

    @Mock
    private RecuperarVotosService recuperarVotosService;

    @Mock
    private SalvarVotoService salvarVotoService;

    @InjectMocks
    private RegistrarVotoUserCaseImpl registrarVotoService;

    @Test
    public void votar_darErroQuandoAPautaNaoExiste() {
        var voto = gerarVotoValido();
        var expected = new PautaNaoEncontradaException(voto.getPautaId());

        when(recuperarPautaService.existe(voto.getPautaId())).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> registrarVotoService.votar(voto))
                .isInstanceOf(PautaNaoEncontradaException.class)
                .hasMessage(expected.getMessage());

        verify(recuperarPautaService, times(1)).existe(any());
        verify(validaCpfService, times(0)).cpfValido(any());
        verify(recuperarSessaoService, times(0)).existePautaAberta(any(), any());
        verify(recuperarVotosService, times(0)).existsByPautaIdAndCpf(any(), any());
        verify(salvarVotoService, times(0)).salvar(any());
    }

    @Test
    public void votar_darErroQuandoOCpfEInvalido() {
        var voto = gerarVotoCPFInvalido();
        var expected = new CpfNaoAutorizadoAVotarException(voto.getCpf());

        when(recuperarPautaService.existe(any())).thenReturn(Boolean.TRUE);
        when(validaCpfService.cpfValido(any())).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> registrarVotoService.votar(voto))
                .isInstanceOf(CpfNaoAutorizadoAVotarException.class)
                .hasMessage(expected.getMessage());

        verify(recuperarPautaService, times(1)).existe(any());
        verify(validaCpfService, times(1)).cpfValido(any());
        verify(recuperarSessaoService, times(0)).existePautaAberta(any(), any());
        verify(recuperarVotosService, times(0)).existsByPautaIdAndCpf(any(), any());
        verify(salvarVotoService, times(0)).salvar(any());
    }

    @Test
    public void votar_darErroQuandoASessaoNaoEstaAberta() {
        var voto = gerarVotoCPFInvalido();
        var expected = new PautaSemSessaoAbertaException(voto.getPautaId());

        when(recuperarPautaService.existe(any())).thenReturn(Boolean.TRUE);
        when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        when(recuperarSessaoService.existePautaAberta(any(), any())).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> registrarVotoService.votar(voto))
                .isInstanceOf(PautaSemSessaoAbertaException.class)
                .hasMessage(expected.getMessage());

        verify(recuperarPautaService, times(1)).existe(any());
        verify(validaCpfService, times(1)).cpfValido(any());
        verify(recuperarSessaoService, times(1)).existePautaAberta(any(), any());
        verify(recuperarVotosService, times(0)).existsByPautaIdAndCpf(any(), any());
        verify(salvarVotoService, times(0)).salvar(any());
    }

    @Test
    public void votar_darErroQuandoOCpfJaTemVoto() {
        var voto = gerarVotoCPFInvalido();
        var expected = new VotoJaRegistradoException(voto.getCpf());

        when(recuperarPautaService.existe(any())).thenReturn(Boolean.TRUE);
        when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        when(recuperarSessaoService.existePautaAberta(any(), any())).thenReturn(Boolean.TRUE);
        when(recuperarVotosService.existsByPautaIdAndCpf(any(), any())).thenReturn(Boolean.TRUE);

        assertThatThrownBy(() -> registrarVotoService.votar(voto))
                .isInstanceOf(VotoJaRegistradoException.class)
                .hasMessage(expected.getMessage());

        verify(recuperarPautaService, times(1)).existe(any());
        verify(validaCpfService, times(1)).cpfValido(any());
        verify(recuperarSessaoService, times(1)).existePautaAberta(any(), any());
        verify(recuperarVotosService, times(1)).existsByPautaIdAndCpf(any(), any());
        verify(recuperarPautaService, times(1)).existe(any());
        verify(salvarVotoService, times(0)).salvar(any());
    }

    @Test
    public void votar_naoDarErro() {
        var voto = gerarVotoCPFInvalido();

        when(recuperarPautaService.existe(any())).thenReturn(Boolean.TRUE);
        when(validaCpfService.cpfValido(any())).thenReturn(Boolean.TRUE);
        when(recuperarSessaoService.existePautaAberta(any(), any())).thenReturn(Boolean.TRUE);
        when(recuperarVotosService.existsByPautaIdAndCpf(any(), any())).thenReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> registrarVotoService.votar(voto));

        verify(recuperarPautaService, times(1)).existe(any());
        verify(validaCpfService, times(1)).cpfValido(any());
        verify(recuperarSessaoService, times(1)).existePautaAberta(any(), any());
        verify(recuperarVotosService, times(1)).existsByPautaIdAndCpf(any(), any());
        verify(recuperarPautaService, times(1)).existe(any());
        verify(salvarVotoService, times(1)).salvar(any());
    }

    public Voto gerarVotoValido() {
        return Voto.builder()
                .cpf(CPF_VALIDO)
                .hora(LocalDateTime.now())
                .pautaId(1L)
                .voto(true)
                .build();
    }

    public Voto gerarVotoCPFInvalido() {
        return Voto.builder()
                .cpf(CPF_INVALIDO)
                .hora(LocalDateTime.now())
                .voto(true)
                .pautaId(1L)
                .build();
    }
}
