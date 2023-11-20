package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.PautaEmVotacao;
import dev.danielarrais.votingsystem.application.exceptions.ResultadosNaoProcessados;
import dev.danielarrais.votingsystem.domain.Resultado;
import dev.danielarrais.votingsystem.infra.database.entities.ResultadoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecuperarResultadoService {
    private final SessaoRepository sessaoRepository;
    private final ResultadoRepository resultadoRepository;

    @Transactional
    public Resultado resultado(Long pautaId) {
        validaSeSessaoDaPautaEstarAberta(pautaId);
        return buscaResultadosDaVotacao(pautaId);
    }

    private Resultado buscaResultadosDaVotacao(Long pautaId) {
        ResultadoEntity resultado = resultadoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new ResultadosNaoProcessados(pautaId));


        return Resultado.builder()
                .resultado(resultado.getResultado())
                .votosFavoraveis(resultado.getVotosFavoraveis())
                .votosContrarios(resultado.getVotosContrarios())
                .build();
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId) {
        boolean pautaAberta = sessaoRepository.sessaoPautaEstarAberta(pautaId);

        if (!pautaAberta) {
            throw new PautaEmVotacao(pautaId);
        }

    }
}
