package dev.danielarrais.votingsystem.core.application;

import dev.danielarrais.votingsystem.core.application.exceptions.PautaEmVotacaoException;
import dev.danielarrais.votingsystem.core.application.exceptions.ResultadosNaoProcessadosException;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.infra.database.entities.ResultadoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecuperarResultadoService {
    private final SessaoRepository sessaoRepository;
    private final ResultadoRepository resultadoRepository;

    @Transactional
    public Resultado buscarResultado(Long pautaId) {
        validaSeSessaoDaPautaEstarAberta(pautaId);
        return buscaResultadosDaVotacao(pautaId);
    }

    private Resultado buscaResultadosDaVotacao(Long pautaId) {
        ResultadoEntity resultado = resultadoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new ResultadosNaoProcessadosException(pautaId));


        return Resultado.builder()
                .resultado(resultado.getResultado())
                .votosFavoraveis(resultado.getVotosFavoraveis())
                .votosContrarios(resultado.getVotosContrarios())
                .build();
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId) {
        boolean pautaAberta = sessaoRepository.sessaoDaPautaEstarAberta(pautaId);

        if (pautaAberta) {
            throw new PautaEmVotacaoException(pautaId);
        }

    }
}
