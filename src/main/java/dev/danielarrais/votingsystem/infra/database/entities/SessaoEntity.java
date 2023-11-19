package dev.danielarrais.votingsystem.infra.database.entities;

import dev.danielarrais.votingsystem.domain.Pauta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SESSOES")
public class SessaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.NONE)
    private LocalDateTime dataInicio;

    @Setter(AccessLevel.NONE)
    private LocalDateTime dataEncerramento;

    @OneToOne
    private PautaEntity pauta;
}
