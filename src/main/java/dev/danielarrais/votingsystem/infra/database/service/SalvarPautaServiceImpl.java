package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.service.out.SalvarPautaService;
import dev.danielarrais.votingsystem.core.domain.Pauta;
import dev.danielarrais.votingsystem.infra.database.mapper.PautaMapper;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalvarPautaServiceImpl implements SalvarPautaService {
    private final PautaRepository pautaRepository;

    @Override
    public void salvar(Pauta pauta) {
        pautaRepository.save(PautaMapper.convert(pauta));
    }
}
