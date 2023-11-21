package dev.danielarrais.votingsystem.core.application.service.in;

import dev.danielarrais.votingsystem.core.domain.Voto;

public interface RegistrarVotoUserCase {
    void votar(Voto voto);
}
