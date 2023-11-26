package dev.danielarrais.votingsystem.infra.database.service;

import dev.danielarrais.votingsystem.core.application.service.out.SalvarResultadoService;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.infra.database.mapper.ResultadoMapper;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalvarResultadoServiceImpl implements SalvarResultadoService {
    private final ResultadoRepository resultadoRepository;
    private final PautaRepository pautaRepository;

    @Override
    public void salvar(Resultado resultado) {
        var pautaEntity = pautaRepository.findById(resultado.getPautaId()).get();
        var resultadoEntity = ResultadoMapper.convert(resultado);

        resultadoEntity.setPauta(pautaEntity);

        resultadoRepository.save(resultadoEntity);
    }
}
