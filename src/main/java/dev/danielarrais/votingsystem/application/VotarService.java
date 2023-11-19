package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.CPFNaoAutorizadoAVotar;
import dev.danielarrais.votingsystem.application.exceptions.PautaNaoEncontrada;
import dev.danielarrais.votingsystem.application.exceptions.PautaSemSessaoAberta;
import dev.danielarrais.votingsystem.application.exceptions.PautaSemSessaoRegistrada;
import dev.danielarrais.votingsystem.domain.Sessao;
import dev.danielarrais.votingsystem.infra.database.entities.SessaoEntity;
import dev.danielarrais.votingsystem.infra.database.entities.VotoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.PautaRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import dev.danielarrais.votingsystem.infra.database.repositories.VotoRepository;
import dev.danielarrais.votingsystem.infra.feign.CPFValido;
import dev.danielarrais.votingsystem.infra.feign.InvertextClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotarService {
    private final InvertextClient invertextClient;
    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;
    private final VotoRepository votoRepository;

    public void votar(Long pautaId, String CPF, boolean voto) {
        LocalDateTime horaDoVoto = LocalDateTime.now();

        validaAutorizacaoDeVoto(CPF);
        validaSePautaExiste(pautaId);
        validaSeSessaoDaPautaEstarAberta(pautaId, horaDoVoto);

        SessaoEntity sessao = getSessaoDaPauta(pautaId);

        VotoEntity votoEntity = VotoEntity.builder()
                .sessao(sessao)
                .voto(voto)
                .CPF(CPF)
                .hora(horaDoVoto)
                .build();

        votoRepository.save(votoEntity);
    }

    private void validaSePautaExiste(Long pautaId) {
        boolean pautaExiste = pautaRepository.existsById(pautaId);

        if (!pautaExiste) {
            throw new PautaNaoEncontrada(pautaId);
        }
    }

    private void validaAutorizacaoDeVoto(String CPF) {
        boolean podeVotar = cpfAutorizadoAVotar(CPF);

        if (!podeVotar) {
            throw new CPFNaoAutorizadoAVotar(CPF);
        }
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId, LocalDateTime horaDoVoto) {
        boolean pautaAberta = sessaoRepository.sessaoEstavaAbertaNoHorario(horaDoVoto);

        if (!pautaAberta) {
            throw new PautaSemSessaoAberta(pautaId);
        }

    }

    private boolean cpfAutorizadoAVotar(String CPF) {
        CPFValido cpfValido = invertextClient.cpfValido(CPF);

        if (cpfValido != null) {
            return cpfValido.getValido();
        }

        return false;
    }

    private SessaoEntity getSessaoDaPauta(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new PautaSemSessaoRegistrada(pautaId));
    }
}
