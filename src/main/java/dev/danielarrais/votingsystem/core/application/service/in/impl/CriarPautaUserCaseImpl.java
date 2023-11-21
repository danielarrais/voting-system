package dev.danielarrais.votingsystem.core.application.service.in.impl;

import dev.danielarrais.votingsystem.core.application.service.in.CriarPautaUserCase;
import dev.danielarrais.votingsystem.core.application.service.out.SalvarPautaService;
import dev.danielarrais.votingsystem.core.domain.Pauta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarPautaUserCaseImpl implements CriarPautaUserCase {

    private final SalvarPautaService salvarPautaService;

    public void cria(Pauta pauta) {
        salvarPautaService.salvar(pauta);
    }
}
