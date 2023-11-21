package dev.danielarrais.votingsystem.core.application.service.in.impl;

import dev.danielarrais.votingsystem.core.application.service.in.RecuperarPautasUseCase;
import dev.danielarrais.votingsystem.core.application.service.out.RecuperarPautaService;
import dev.danielarrais.votingsystem.core.domain.Pauta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecuperarPautasUseCaseImpl implements RecuperarPautasUseCase {
    private final RecuperarPautaService recuperarPautaService;

    @Override
    public List<Pauta> buscarTodasPautas() {
        return recuperarPautaService.buscarTodasPautas();
    }
}
