package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.CPFNaoAutorizadoAVotarException;
import dev.danielarrais.votingsystem.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.application.exceptions.PautaSemSessaoAbertaException;
import dev.danielarrais.votingsystem.application.exceptions.VotoJaRegistradoException;
import dev.danielarrais.votingsystem.domain.Voto;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
import dev.danielarrais.votingsystem.infra.feign.CPFValido;
import dev.danielarrais.votingsystem.infra.feign.InvertextClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegistrarVotoServiceTests {

    private final String CPF_VALIDO = "064127324234";
    private final String CPF_INVALIDO = "345345345345";

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private InvertextClient invertextClient;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private RegistrarVotoService registrarVotoService;

    @Test
    public void votar_darErroQuandoASessaEstarAberta() {
        var voto = gerarVotoValido();
        var expected = new PautaNaoEncontradaException(1L);

        when(pautaRepository.existsById(1L)).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> registrarVotoService.votar(1L, voto))
                .isInstanceOf(PautaNaoEncontradaException.class)
                .hasMessage(expected.getMessage());

        verify(pautaRepository, times(1)).existsById(any());
        verify(invertextClient, times(0)).cpfValido(any());
        verify(sessaoRepository, times(0)).sessaoPautaEstarAberta(any());
        verify(votoRepository, times(0)).existsByPautaIdAndCpf(any(), any());
        verify(pautaRepository, times(0)).findById(any());
        verify(votoRepository, times(0)).save(any());
    }

    @Test
    public void votar_darErroQuandoOCpfEInvalido() {
        var voto = gerarVotoCPFInvalido();
        var expected = new CPFNaoAutorizadoAVotarException(voto.getCpf());

        when(pautaRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(invertextClient.cpfValido(any())).thenReturn(getValidacaoCPF(false));

        assertThatThrownBy(() -> registrarVotoService.votar(1L, voto))
                .isInstanceOf(CPFNaoAutorizadoAVotarException.class)
                .hasMessage(expected.getMessage());

        verify(pautaRepository, times(1)).existsById(any());
        verify(invertextClient, times(1)).cpfValido(any());
        verify(sessaoRepository, times(0)).sessaoPautaEstarAberta(any(), any());
        verify(votoRepository, times(0)).existsByPautaIdAndCpf(any(), any());
        verify(pautaRepository, times(0)).findById(any());
        verify(votoRepository, times(0)).save(any());
    }

    @Test
    public void votar_darErroQuandoASessaoNaoEstaAberta() {
        var voto = gerarVotoCPFInvalido();
        var expected = new PautaSemSessaoAbertaException(1L);

        when(pautaRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(invertextClient.cpfValido(any())).thenReturn(getValidacaoCPF(Boolean.TRUE));
        when(sessaoRepository.sessaoPautaEstarAberta(any(), any())).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> registrarVotoService.votar(1L, voto))
                .isInstanceOf(PautaSemSessaoAbertaException.class)
                .hasMessage(expected.getMessage());

        verify(pautaRepository, times(1)).existsById(any());
        verify(invertextClient, times(1)).cpfValido(any());
        verify(sessaoRepository, times(1)).sessaoPautaEstarAberta(any(), any());
        verify(votoRepository, times(0)).existsByPautaIdAndCpf(any(), any());
        verify(pautaRepository, times(0)).findById(any());
        verify(votoRepository, times(0)).save(any());
    }

    @Test
    public void votar_darErroQuandoOCpfJaTemVoto() {
        var voto = gerarVotoCPFInvalido();
        var expected = new VotoJaRegistradoException(voto.getCpf());

        when(pautaRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(invertextClient.cpfValido(any())).thenReturn(getValidacaoCPF(Boolean.TRUE));
        when(sessaoRepository.sessaoPautaEstarAberta(any(), any())).thenReturn(Boolean.TRUE);
        when(votoRepository.existsByPautaIdAndCpf(any(), any())).thenReturn(Boolean.TRUE);

        assertThatThrownBy(() -> registrarVotoService.votar(1L, voto))
                .isInstanceOf(VotoJaRegistradoException.class)
                .hasMessage(expected.getMessage());

        verify(pautaRepository, times(1)).existsById(any());
        verify(invertextClient, times(1)).cpfValido(any());
        verify(sessaoRepository, times(1)).sessaoPautaEstarAberta(any(), any());
        verify(votoRepository, times(1)).existsByPautaIdAndCpf(any(), any());
        verify(pautaRepository, times(0)).findById(any());
        verify(votoRepository, times(0)).save(any());
    }

    @Test
    public void votar_naoDarErro() {
        var voto = gerarVotoCPFInvalido();

        when(pautaRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(invertextClient.cpfValido(any())).thenReturn(getValidacaoCPF(Boolean.TRUE));
        when(sessaoRepository.sessaoPautaEstarAberta(any(), any())).thenReturn(Boolean.TRUE);
        when(votoRepository.existsByPautaIdAndCpf(any(), any())).thenReturn(Boolean.FALSE);
        when(pautaRepository.findById(any())).thenReturn(Optional.of(new PautaEntity()));

        assertDoesNotThrow(() -> registrarVotoService.votar(1L, voto));

        verify(pautaRepository, times(1)).existsById(any());
        verify(invertextClient, times(1)).cpfValido(any());
        verify(sessaoRepository, times(1)).sessaoPautaEstarAberta(any(), any());
        verify(votoRepository, times(1)).existsByPautaIdAndCpf(any(), any());
        verify(pautaRepository, times(1)).findById(any());
        verify(votoRepository, times(1)).save(any());
    }

    public Voto gerarVotoValido() {
        return Voto.builder()
                .cpf(CPF_VALIDO)
                .hora(LocalDateTime.now())
                .voto(true)
                .build();
    }

    public Voto gerarVotoCPFInvalido() {
        return Voto.builder()
                .cpf(CPF_INVALIDO)
                .hora(LocalDateTime.now())
                .voto(true)
                .build();
    }

    public CPFValido getValidacaoCPF(boolean valido) {
        return new CPFValido(valido);
    }

}
