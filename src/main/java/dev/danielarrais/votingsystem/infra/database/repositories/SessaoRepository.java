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

    @Query("select exists(s.id) from SessaoEntity s where s.dataEncerramento <= :date")
    Boolean sessaoEstavaAbertaNoHorario(LocalDateTime date);
}