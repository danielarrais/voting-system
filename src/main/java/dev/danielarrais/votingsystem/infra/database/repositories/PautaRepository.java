package dev.danielarrais.votingsystem.infra.database.repositories;

import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import dev.danielarrais.votingsystem.infra.database.entities.SessaoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PautaRepository extends JpaRepository<PautaEntity, Long> {

    @Query("select p.id from PautaEntity p " +
            "left join p.resultado r " +
            "join SessaoEntity s on s.pauta.id = p.id and current_timestamp > s.dataEncerramento " +
            "where r.id is null" )
    List<Long> buscarPautasSemResultados();

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select p from PautaEntity  p where p.id = :pautaId")
    PautaEntity findLockById(Long pautaId);
}