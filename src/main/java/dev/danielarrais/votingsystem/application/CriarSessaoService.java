package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.PautaComSessaoJaRegistradaException;
import dev.danielarrais.votingsystem.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.domain.Sessao;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.SessaoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarSessaoService {
    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;

    public void criar(Long pautaId, Sessao sessao) {
        PautaEntity pautaEntity = buscarPauta(pautaId);

        validaUnicidadeDaSessao(pautaId);

        SessaoEntity sessaoEntity = SessaoEntity.builder()
                .dataInicio(sessao.getDataInicio())
                .dataEncerramento(sessao.getDataEncerramento())
                .pauta(pautaEntity)
                .build();

        sessaoRepository.save(sessaoEntity);
    }

    private void validaUnicidadeDaSessao(Long pautaId) {
        boolean jaExisteSessao = sessaoRepository.existsByPautaId(pautaId);

        if (jaExisteSessao) {
            throw new PautaComSessaoJaRegistradaException(pautaId);
        }

    }

    private PautaEntity buscarPauta(Long pautaId) {
        return pautaRepository.findById(pautaId).orElseThrow(() -> new PautaNaoEncontradaException(pautaId));
    }
}
