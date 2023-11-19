package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.PautaNaoEncontrada;
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

    public void cria(Long pautaID, Integer duracao) {
        validarSePautaExiste(pautaID);

        LocalDateTime dataInicio = LocalDateTime.now();

        // TODO: CRIAR DURACAO DEFAULT DE 1 MINUTO
        LocalDateTime dataEncerramento = gerarDataEncerramento(duracao, dataInicio);

        SessaoEntity sessaoEntity = SessaoEntity.builder()
                .dataInicio(dataInicio)
                .dataEncerramento(dataEncerramento)
                .build();

        sessaoRepository.save(sessaoEntity);
    }

    private void validarSePautaExiste(Long pautaId) {
        boolean pautaExiste = pautaRepository.existsById(pautaId);
        if (!pautaExiste) {
            throw new PautaNaoEncontrada("Pauta de ID " + pautaId + "Não encontrada");
        }
    }

    private LocalDateTime gerarDataEncerramento(Integer duracao, LocalDateTime dataInicio) {
        return dataInicio.plusMinutes(duracao);
    }
}
