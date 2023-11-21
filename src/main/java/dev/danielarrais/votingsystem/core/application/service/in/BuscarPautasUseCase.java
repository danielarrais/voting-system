package dev.danielarrais.votingsystem.core.application.service.in;

import dev.danielarrais.votingsystem.core.domain.Pauta;

import java.util.List;

public interface BuscarPautasUseCase {
    List<Pauta> buscarTodasPautas();
}
