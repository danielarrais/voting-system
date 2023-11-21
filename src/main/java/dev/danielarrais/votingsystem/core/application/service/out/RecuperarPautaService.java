package dev.danielarrais.votingsystem.core.application.service.out;

import dev.danielarrais.votingsystem.core.domain.Pauta;

import java.util.List;

public interface RecuperarPautaService {
    boolean existe(Long pautaId);
    List<Long> buscarPautasSemResultados();
    List<Pauta> buscarTodasPautas();
}
