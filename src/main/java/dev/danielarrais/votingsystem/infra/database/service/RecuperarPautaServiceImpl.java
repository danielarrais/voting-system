package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarPautaService;
import dev.danielarrais.votingsystem.core.domain.Pauta;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.mapper.PautaMapper;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecuperarPautaServiceImpl implements RecuperarPautaService {
    private final PautaRepository pautaRepository;

    public PautaEntity buscarPauta(Long pautaId) {
        return pautaRepository.findById(pautaId).orElseThrow(() -> new PautaNaoEncontradaException(pautaId));
    }

    public List<Long> buscarPautasSemResultados() {
        return pautaRepository.buscarPautasSemResultados();
    }

    @Override
    public List<Pauta> buscarTodasPautas() {
        var pautasEntities = pautaRepository.findAll();
        return PautaMapper.convert(pautasEntities);
    }

    @Override
    public boolean existe(Long pautaId) {
        return pautaRepository.existsById(pautaId);
    }
}
