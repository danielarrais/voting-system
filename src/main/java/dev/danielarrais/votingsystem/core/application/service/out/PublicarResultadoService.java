package dev.danielarrais.votingsystem.core.application.service.out;

import dev.danielarrais.votingsystem.core.domain.Resultado;

public interface PublicarResultadoService {
    void publicar(Resultado resultado);
}
