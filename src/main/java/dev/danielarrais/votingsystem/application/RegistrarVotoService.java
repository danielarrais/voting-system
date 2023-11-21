package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.application.exceptions.CPFNaoAutorizadoAVotarException;
import dev.danielarrais.votingsystem.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.application.exceptions.PautaSemSessaoAbertaException;
import dev.danielarrais.votingsystem.application.exceptions.VotoJaRegistradoException;
import dev.danielarrais.votingsystem.domain.Voto;
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

    public void votar(Long pautaId, Voto voto) {
        LocalDateTime horaDoVoto = LocalDateTime.now();

        validaSePautaExiste(pautaId);
        validaAutorizacaoDeVoto(voto.getCpf());
        validaSeSessaoDaPautaEstarAberta(pautaId, horaDoVoto);
        validaSeJaVotou(pautaId, voto.getCpf());

        PautaEntity pautaEntity = pautaRepository.findById(pautaId).get();

        VotoEntity votoEntity = VotoEntity.builder()
                .pauta(pautaEntity)
                .voto(voto.getVoto())
                .cpf(voto.getCpf())
                .hora(horaDoVoto)
                .build();

        votoRepository.save(votoEntity);
    }

    private void validaSeJaVotou(Long pautaId, String cpf) {
        boolean jaVotou = votoRepository.existsByPautaIdAndCpf(pautaId, cpf);

        if (jaVotou) {
            throw new VotoJaRegistradoException(cpf);
        }
    }

    private void validaSePautaExiste(Long pautaId) {
        boolean pautaExiste = pautaRepository.existsById(pautaId);

        if (!pautaExiste) {
            throw new PautaNaoEncontradaException(pautaId);
        }
    }

    private void validaAutorizacaoDeVoto(String CPF) {
        boolean podeVotar = cpfAutorizadoAVotar(CPF);

        if (!podeVotar) {
            throw new CPFNaoAutorizadoAVotarException(CPF);
        }
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId, LocalDateTime horaDoVoto) {
        boolean pautaAberta = sessaoRepository.sessaoPautaEstarAberta(pautaId, horaDoVoto);

        if (!pautaAberta) {
            throw new PautaSemSessaoAbertaException(pautaId);
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
