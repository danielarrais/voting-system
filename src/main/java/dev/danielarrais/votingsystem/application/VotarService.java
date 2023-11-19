package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.application.exceptions.CPFNaoAutorizadoAVotar;
import dev.danielarrais.votingsystem.domain.Sessao;
import dev.danielarrais.votingsystem.infra.database.entities.SessaoEntity;
import dev.danielarrais.votingsystem.infra.database.repositories.SessaoRepository;
import dev.danielarrais.votingsystem.infra.feign.CPFValido;
import dev.danielarrais.votingsystem.infra.feign.InvertextClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotarService {
    private final InvertextClient invertextClient;
    private final SessaoRepository sessaoRepository;

    public void votar(Long pautaId, String CPF, boolean voto) {

    }
}
