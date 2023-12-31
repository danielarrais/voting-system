package dev.danielarrais.votingsystem.infra.database.repositories;

import dev.danielarrais.votingsystem.infra.database.entities.ResultadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoEntity, Long> {
    Optional<ResultadoEntity> findByPautaId(Long pautaId);
}