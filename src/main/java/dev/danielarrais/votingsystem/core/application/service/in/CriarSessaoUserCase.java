package dev.danielarrais.votingsystem.core.application.service.in;

import dev.danielarrais.votingsystem.core.domain.Sessao;

public interface CriarSessaoUserCase {
    void criar(Long pautaId, Sessao sessao);

}
