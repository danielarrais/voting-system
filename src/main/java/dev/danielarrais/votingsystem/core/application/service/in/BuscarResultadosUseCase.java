package dev.danielarrais.votingsystem.core.application.service.in;

import dev.danielarrais.votingsystem.core.domain.Resultado;

public interface BuscarResultadosUseCase {
    Resultado buscarResultado(Long pautaId);
}
