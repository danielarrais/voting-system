package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.exceptions.ResultadosNaoProcessadosException;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarResultadoService;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.infra.database.mapper.ResultadoMapper;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecuperarResultadoServiceImpl implements RecuperarResultadoService {
    private final ResultadoRepository resultadoRepository;

    @Override
    public Resultado buscarResultadoDaPauta(Long pautaId) {
        var resultadoEntity = resultadoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new ResultadosNaoProcessadosException(pautaId));
        return ResultadoMapper.convert(resultadoEntity);
    }
}
