package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.service.out.RecuperarVotosService;
import dev.danielarrais.votingsystem.core.domain.Voto;
import dev.danielarrais.votingsystem.infra.database.mapper.VotoMapper;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecuperarVotosServiceImpl implements RecuperarVotosService {

    private final VotoRepository votoRepository;

    @Override
    public boolean existsByPautaIdAndCpf(Long pautaId, String cpf) {
        return votoRepository.existsByPautaIdAndCpf(pautaId, cpf);
    }

    public List<Voto> buscaVotosDaPauta(Long pautaId) {
        var votos = votoRepository.findByPautaId(pautaId);
        return VotoMapper.convert(votos);
    }
}
