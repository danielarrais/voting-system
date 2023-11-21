package dev.danielarrais.votingsystem.core.application.service.out;

import dev.danielarrais.votingsystem.core.domain.Voto;

import java.util.List;

public interface RecuperarVotosService {
    boolean existsByPautaIdAndCpf(Long pautaId, String cpf);
    List<Voto> buscaVotosDaPauta(Long pautaId);
}
