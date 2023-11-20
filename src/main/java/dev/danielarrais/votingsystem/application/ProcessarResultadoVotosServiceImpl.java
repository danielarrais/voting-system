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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class ProcessarResultadoVotosServiceImpl implements ProcessarResultadoVotosService {
    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final ResultadoRepository resultadoRepository;

    @Transactional
    public void resultado(Long pautaId) {
        validaSeSessaoDaPautaEstarAberta(pautaId);

        List<VotoEntity> votos = votoRepository.findByPautaId(pautaId);
        PautaEntity pautaEntity = pautaRepository.findLockById(pautaId);

        ResultadoEntity resultado = processarVotos(votos, pautaEntity);

        resultadoRepository.save(resultado);

        log.info("Resultados da pauta {} processados às {}", pautaId, LocalDateTime.now());
    }

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void processarResultadosVotacoes() {
        var pautas = pautaRepository.buscarPautasSemResultados();
        pautas.forEach(this::resultado);
    }

    public ResultadoEntity processarVotos(List<VotoEntity> votos, PautaEntity pauta) {
        var contagemDeVotos = contaVotos(votos);
        var resultado = processaResultado(contagemDeVotos);

        return ResultadoEntity.builder()
                .votosFavoraveis(contagemDeVotos.getLeft())
                .votosContrarios(contagemDeVotos.getRight())
                .resultado(resultado.name())
                .pauta(pauta)
                .build();
    }

    private Pair<Integer, Integer> contaVotos(List<VotoEntity> votos) {
        int votosFavoraveis = 0;
        int votosContra = 0;

        for (VotoEntity voto : votos) {
            if (voto.getVoto()) {
                votosFavoraveis++;
            } else {
                votosContra++;
            }
        }

        return Pair.of(votosFavoraveis, votosContra);
    }

    private ResultadoEnum processaResultado(Pair<Integer, Integer> contagemDeVotos) {
        if (contagemDeVotos.getLeft().equals(0) && contagemDeVotos.getRight().equals(0) ) {
            return ResultadoEnum.ABSTENCAO;
        }

        if (contagemDeVotos.getLeft() > contagemDeVotos.getRight()) {
            return ResultadoEnum.APROVADA;
        }

        if (contagemDeVotos.getLeft().equals(contagemDeVotos.getRight())) {
            return ResultadoEnum.EMPATADA;
        }

        return ResultadoEnum.REPROVADA;
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId) {
        boolean pautaAberta = sessaoRepository.sessaoPautaEstarAberta(pautaId);

        if (pautaAberta) {
            throw new PautaEmVotacaoException(pautaId);
        }

    }
}
