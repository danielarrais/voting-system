package dev.danielarrais.votingsystem.core.application.service.in.impl;

import dev.danielarrais.votingsystem.core.application.exceptions.PautaComSessaoJaRegistradaException;
import dev.danielarrais.votingsystem.core.application.service.in.CriarSessaoUserCase;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarSessaoService;
import dev.danielarrais.votingsystem.core.application.service.out.RegistraSessaoService;
import dev.danielarrais.votingsystem.core.domain.Sessao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarSessaoUserCaseImpl implements CriarSessaoUserCase {
    private final RecuperarSessaoService recuperarSessaoService;
    private final RegistraSessaoService registraSessaoService;

    public void criar(Long pautaId, Sessao sessao) {
        validaUnicidadeDaSessao(pautaId);
        registraSessaoService.registrar(pautaId, sessao);
    }

    private void validaUnicidadeDaSessao(Long pautaId) {
        boolean jaExisteSessao = recuperarSessaoService.existePautaPeloId(pautaId);

        if (jaExisteSessao) {
            throw new PautaComSessaoJaRegistradaException(pautaId);
        }
    }


}
