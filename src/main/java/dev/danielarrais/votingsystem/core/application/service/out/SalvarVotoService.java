package dev.danielarrais.votingsystem.core.application.service.out;

import dev.danielarrais.votingsystem.core.domain.Voto;

public interface SalvarVotoService {
    void salvar(Voto votoEntity);
}
