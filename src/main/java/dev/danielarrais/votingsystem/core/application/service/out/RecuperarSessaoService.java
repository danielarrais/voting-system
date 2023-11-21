package dev.danielarrais.votingsystem.core.application.service.out;

import java.time.LocalDateTime;

public interface RecuperarSessaoService {
    boolean existePautaPeloId(Long pautaId);
    boolean existePautaAberta(Long pautaId, LocalDateTime horaDoVoto);
    boolean sessaoDaPautaEstarAberta(Long pautaId);
}
