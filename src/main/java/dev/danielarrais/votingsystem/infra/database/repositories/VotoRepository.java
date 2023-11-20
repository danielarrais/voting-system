package dev.danielarrais.votingsystem.infra.database.repositories;

import dev.danielarrais.votingsystem.infra.database.entities.VotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<VotoEntity, Long> {
    boolean existsByPautaIdAndCpf(Long pautaId, String cpf);

    List<VotoEntity> findByPautaId(Long pautaId);
}