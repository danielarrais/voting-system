package dev.danielarrais.votingsystem.core.application.service.in.impl;

import dev.danielarrais.votingsystem.core.application.exceptions.PautaEmVotacaoException;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.core.application.service.in.BuscarResultadosUseCase;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarPautaService;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarResultadoService;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarSessaoService;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuscarResultadoUseCaseImpl implements BuscarResultadosUseCase {
    private final RecuperarSessaoService recuperarSessaoService;
    private final RecuperarResultadoService recuperarResultadoService;
    private final RecuperarPautaService recuperarPautaService;

    @Transactional
    public Resultado buscarResultado(Long pautaId) {
        validaSeSessaoDaPautaEstarAberta(pautaId);
        validaSePautaExiste(pautaId);
        return buscaResultadosDaVotacao(pautaId);
    }

    private void validaSePautaExiste(Long pautaId) {
        boolean pautaExiste = recuperarPautaService.existe(pautaId);

        if (!pautaExiste) {
            throw new PautaNaoEncontradaException(pautaId);
        }
    }

    private Resultado buscaResultadosDaVotacao(Long pautaId) {
        return recuperarResultadoService.buscarResultadoDaPauta(pautaId);
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId) {
        boolean pautaAberta = recuperarSessaoService.sessaoDaPautaEstarAberta(pautaId);

        if (pautaAberta) {
            throw new PautaEmVotacaoException(pautaId);
        }

    }
}
