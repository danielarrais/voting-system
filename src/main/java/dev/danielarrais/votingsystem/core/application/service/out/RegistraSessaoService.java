package dev.danielarrais.votingsystem.core.application.service.out;

import dev.danielarrais.votingsystem.core.domain.Sessao;

public interface RegistraSessaoService {
    void registrar(Long pautaId, Sessao sessao);
}
