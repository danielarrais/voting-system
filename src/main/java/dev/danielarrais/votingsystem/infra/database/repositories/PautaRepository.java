package dev.danielarrais.votingsystem.infra.database.repositories;

import dev.danielarrais.votingsystem.infra.database.entities.PautaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<PautaEntity, Long> {

    @Query("select p.id from PautaEntity p " +
            "left join p.resultado r " +
            "join SessaoEntity s on s.pauta.id = p.id and current_timestamp > s.dataEncerramento " +
            "where r.id is null" )
    List<Long> buscarPautasSemResultados();
}