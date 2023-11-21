package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.service.out.RegistraSessaoService;
import dev.danielarrais.votingsystem.core.domain.Sessao;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.mapper.SessaoMapper;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistraSessaoServiceImpl implements RegistraSessaoService {
    private final RecuperarPautaServiceImpl recuperarPautaService;
    private final SessaoRepository sessaoRepository;

    @Override
    public void registrar(Long pautaId, Sessao sessao) {
        PautaEntity pautaEntity = recuperarPautaService.buscarPauta(pautaId);
        sessaoRepository.save(SessaoMapper.convert(sessao, pautaEntity));
    }
}
