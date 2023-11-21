package dev.danielarrais.votingsystem.infra.database.repositories;

import dev.danielarrais.votingsystem.infra.database.entities.SessaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoEntity, Long> {
    Optional<SessaoEntity> findByPautaId(Long pautaId);

    @Query("select (count(s) > 0) from SessaoEntity s where :date <= s.dataEncerramento and s.pauta.id = :pautaId")
    boolean sessaoDaPautaEstarAberta(Long pautaId, LocalDateTime date);

    @Query("select (count(s) > 0) from SessaoEntity s where CURRENT_TIMESTAMP <= s.dataEncerramento and s.pauta.id = :pautaId")
    boolean sessaoDaPautaEstarAberta(Long pautaId);

    boolean existsByPautaId(Long pautaId);
}