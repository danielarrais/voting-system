package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.application.exceptions.*;
import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
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
public class RegistrarVotoService {
    private final InvertextClient invertextClient;
    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;
    private final VotoRepository votoRepository;

    public void votar(Long pautaId, VotoRequest votoRequest) {
        LocalDateTime horaDoVoto = LocalDateTime.now();

        validaAutorizacaoDeVoto(votoRequest.getCpf());
        validaSePautaExiste(pautaId);
        validaSeSessaoDaPautaEstarAberta(pautaId, horaDoVoto);
        validaSeJaVotou(pautaId, votoRequest.getCpf());

        PautaEntity pautaEntity = pautaRepository.findById(pautaId).get();

        VotoEntity votoEntity = VotoEntity.builder()
                .pauta(pautaEntity)
                .voto(votoRequest.getVoto())
                .cpf(votoRequest.getCpf())
                .hora(horaDoVoto)
                .build();

        votoRepository.save(votoEntity);
    }

    private void validaSeJaVotou(Long pautaId, String cpf) {
        boolean jaVotou = votoRepository.existsByPautaIdAndCpf(pautaId, cpf);

        if (jaVotou) {
            throw new VotoJaRegistrado(cpf);
        }
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
}
