package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.dto.ResultadoEnum;
import dev.danielarrais.votingsystem.application.exceptions.PautaEmVotacao;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.ResultadoEntity;
import dev.danielarrais.votingsystem.infra.database.entities.VotoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class ProcessarResultadoVotosService {
    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final ResultadoRepository resultadoRepository;

    @Transactional
    public void resultado(Long pautaId) {
        validaSeSessaoDaPautaEstarAberta(pautaId);
        PautaEntity pautaEntity = pautaRepository.findLockById(pautaId);

        List<VotoEntity> votos = votoRepository.findByPautaId(pautaId);
        ResultadoEntity resultado = processarVotos(votos, pautaEntity);

        resultadoRepository.save(resultado);

        log.info("Resultados da pauta {} processados às {}", pautaId, LocalDateTime.now());
    }

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void processarResultadosVotacoes() {
        List<Long> pautas = pautaRepository.buscarPautasSemResultados();
        pautas.forEach(this::resultado);
    }

    private ResultadoEntity processarVotos(List<VotoEntity> votos, PautaEntity pauta) {
        int votosFavoraveis = 0;
        int votosContra = 0;

        for (VotoEntity voto : votos) {
            if (voto.getVoto()) {
                votosFavoraveis++;
            } else {
                votosContra++;
            }
        }

        ResultadoEnum resultado = processaResultado(votosFavoraveis, votosContra);

        return ResultadoEntity.builder()
                .votosFavoraveis(votosFavoraveis)
                .votosContrarios(votosContra)
                .resultado(resultado.name())
                .pauta(pauta)
                .build();
    }

    private ResultadoEnum processaResultado(int votosFavoraveis, int votosContra) {
        if (votosFavoraveis == 0 && votosContra == 0) {
            return ResultadoEnum.SEM_VOTOS;
        }

        if (votosFavoraveis > votosContra) {
            return ResultadoEnum.APROVADA;
        }

        if (votosFavoraveis == votosContra) {
            return ResultadoEnum.EMPATADA;
        }

        return ResultadoEnum.REPROVADA;
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId) {
        boolean pautaAberta = sessaoRepository.sessaoPautaEstarAberta(pautaId);

        if (!pautaAberta) {
            throw new PautaEmVotacao(pautaId);
        }

    }
}
