package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.service.out.SalvarVotoService;
import dev.danielarrais.votingsystem.core.domain.Voto;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.VotoEntity;
import dev.danielarrais.votingsystem.infra.database.mapper.VotoMapper;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalvarVotoServiceImpl implements SalvarVotoService {
    private final RecuperarPautaServiceImpl recuperarPautaService;
    private final VotoRepository votoRepository;
    @Override
    public void salvar(Voto voto) {
        var pautaEntity = recuperarPautaService.buscarPauta(voto.getPautaId());
        votoRepository.save(VotoMapper.convert(pautaEntity,voto));
    }
}
