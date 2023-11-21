package dev.danielarrais.votingsystem.core.application.service.in.impl;

import dev.danielarrais.votingsystem.core.application.dto.ResultadoEnum;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaEmVotacaoException;
import dev.danielarrais.votingsystem.core.application.service.in.ProcessarResultadoVotosUseCase;
import dev.danielarrais.votingsystem.core.application.service.out.*;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.core.domain.Voto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class ProcessarResultadoVotosUseCaseImpl implements ProcessarResultadoVotosUseCase {

    private final RecuperarPautaService recuperarPautaService;
    private final RecuperarVotosService recuperarVotosService;
    private final RecuperarSessaoService recuperarSessaoService;
    private final SalvarResultadoService salvarResultadoService;
    private final PublicarResultadoService publicarResultadoService;

    @Transactional
    public Resultado processarResultado(Long pautaId) {
        validaSeSessaoDaPautaEstarAberta(pautaId);

        var votos = recuperarVotosService.buscaVotosDaPauta(pautaId);
        var resultado = processarVotos(votos, pautaId);

        salvarResultadoService.salvar(resultado);

        return resultado;
    }

    private void publicar(Resultado resultado) {
        publicarResultadoService.publicar(resultado);
    }

    public void publicar() {
        recuperarPautaService.buscarPautasSemResultados()
                .forEach(this::publicarResultado);
    }

    @Transactional
    public void publicarResultado(Long pautaId) {
        var resultado = processarResultado(pautaId);
        try {
            publicar(resultado);
        } catch (Exception e) {
            log.error("Falha a publicar resultado", e);
        }
    }

    public Resultado processarVotos(List<Voto> votos, Long pautaId) {
        var contagemDeVotos = contaVotos(votos);
        var resultado = processaResultado(contagemDeVotos);

        return Resultado.builder()
                .votosFavoraveis(contagemDeVotos.getLeft())
                .votosContrarios(contagemDeVotos.getRight())
                .resultado(resultado.name())
                .pautaId(pautaId)
                .build();
    }

    private Pair<Integer, Integer> contaVotos(List<Voto> votos) {
        int votosFavoraveis = 0;
        int votosContra = 0;

        for (Voto voto : votos) {
            if (voto.getVoto()) {
                votosFavoraveis++;
            } else {
                votosContra++;
            }
        }

        return Pair.of(votosFavoraveis, votosContra);
    }

    private ResultadoEnum processaResultado(Pair<Integer, Integer> contagemDeVotos) {
        return ResultadoEnum.getResult(contagemDeVotos.getLeft(), contagemDeVotos.getRight());
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId) {
        boolean pautaAberta = recuperarSessaoService.sessaoDaPautaEstarAberta(pautaId);

        if (pautaAberta) {
            throw new PautaEmVotacaoException(pautaId);
        }

    }
}
