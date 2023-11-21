package dev.danielarrais.votingsystem.core.application.service.in;

import dev.danielarrais.votingsystem.core.application.dto.ResultadoEnum;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaEmVotacaoException;
import dev.danielarrais.votingsystem.core.application.exceptions.ResultadosNaoProcessadosException;
import dev.danielarrais.votingsystem.core.application.service.in.impl.BuscarResultadoUseCaseImpl;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarResultadoService;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarSessaoService;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.infra.database.entities.ResultadoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BuscarResultadoUseCaseTests {

    @Mock
    private RecuperarSessaoService sessaoRepository;

    @Mock
    private RecuperarResultadoService resultadoRepository;

    @InjectMocks
    private BuscarResultadoUseCaseImpl resultadoService;

    @Test
    public void buscarResultado_darErroQuandoASessaEstarAberta() {
        Mockito.when(sessaoRepository.sessaoDaPautaEstarAberta(1L)).thenReturn(Boolean.TRUE);

        var expected = new PautaEmVotacaoException(1L);

        assertThatThrownBy(() -> resultadoService.buscarResultado(1L))
                .isInstanceOf(PautaEmVotacaoException.class)
                .hasMessage(expected.getMessage());
    }

    @Test
    public void buscarResultado_trazOsResultados() {
        var resultadoEsperado = Resultado.builder()
                .resultado(ResultadoEnum.REPROVADA.name())
                .votosFavoraveis(2)
                .votosContrarios(1)
                .build();

        Mockito.when(sessaoRepository.sessaoDaPautaEstarAberta(1L)).thenReturn(Boolean.FALSE);
        Mockito.when(resultadoRepository.buscarResultadoDaPauta(1L)).thenReturn(resultadoEsperado);

        var resultado = resultadoService.buscarResultado(1L);

        assertThat(resultado)
                .extracting(Resultado::getResultado,
                        Resultado::getVotosContrarios,
                        Resultado::getVotosFavoraveis)
                .containsExactly(resultadoEsperado.getResultado(),
                        resultadoEsperado.getVotosContrarios(),
                        resultadoEsperado.getVotosFavoraveis());
    }


}
