package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.service.out.RecuperarSessaoService;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecuperarSessaoServiceImpl implements RecuperarSessaoService {
    private final SessaoRepository sessaoRepository;

    @Override
    public boolean existePautaPeloId(Long pautaId) {
        return sessaoRepository.existsByPautaId(pautaId);
    }

    @Override
    public boolean existePautaAberta(Long pautaId, LocalDateTime horaDoVoto) {
        return sessaoRepository.sessaoDaPautaEstarAberta(pautaId, horaDoVoto);
    }

    @Override
    public boolean sessaoDaPautaEstarAberta(Long pautaId) {
        return sessaoRepository.sessaoDaPautaEstarAberta(pautaId);
    }
}
