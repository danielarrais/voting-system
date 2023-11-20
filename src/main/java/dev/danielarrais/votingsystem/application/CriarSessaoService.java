package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.PautaComSessaoJaRegistrada;
import dev.danielarrais.votingsystem.application.exceptions.PautaNaoEncontrada;
import dev.danielarrais.votingsystem.application.exceptions.PautaSemSessaoAberta;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
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

    public void cria(Long pautaId, Integer duracao) {
        validaSePautaExiste(pautaId);
        validaUnicidadeDaSessao(pautaId);

        PautaEntity pautaEntity = buscarPauta(pautaId);
        LocalDateTime dataInicio = LocalDateTime.now();

        // TODO: CRIAR DURACAO DEFAULT DE 1 MINUTO
        LocalDateTime dataEncerramento = gerarDataEncerramento(duracao, dataInicio);

        SessaoEntity sessaoEntity = SessaoEntity.builder()
                .dataInicio(dataInicio)
                .dataEncerramento(dataEncerramento)
                .pauta(pautaEntity)
                .build();

        sessaoRepository.save(sessaoEntity);
    }

    private void validaSePautaExiste(Long pautaId) {
        boolean pautaExiste = pautaRepository.existsById(pautaId);

        if (!pautaExiste) {
            throw new PautaNaoEncontrada(pautaId);
        }
    }

    private void validaUnicidadeDaSessao(Long pautaId) {
        boolean jaExisteSessao = sessaoRepository.existsByPautaId(pautaId);

        if (jaExisteSessao) {
            throw new PautaComSessaoJaRegistrada(pautaId);
        }

    }

    private PautaEntity buscarPauta(Long pautaId) {
        return pautaRepository.findById(pautaId).orElseThrow(() -> new PautaNaoEncontrada(pautaId));
    }

    private LocalDateTime gerarDataEncerramento(Integer duracao, LocalDateTime dataInicio) {
        return dataInicio.plusMinutes(duracao);
    }
}
