package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.application.exceptions.AssembleiaNaoEncontrada;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.AssembleiaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarPautaService {
    private final PautaRepository pautaRepository;
    private final AssembleiaRepository assembleiaRepository;

    public void cria(PautaRequest pautaRequest) {
        PautaEntity pautaEntity = PautaEntity.builder()
                .titulo(pautaRequest.getTitulo())
                .descricao(pautaRequest.getDescricao())
                .build();

        pautaRepository.save(pautaEntity);
    }

    public void validaSeAssembleiaExiste(Long assembleiaId) {
        boolean assembleiaExiste = assembleiaRepository.existsById(assembleiaId);

        if (!assembleiaExiste) {
            throw new AssembleiaNaoEncontrada(assembleiaId);
        }
    }
}
