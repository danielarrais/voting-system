package dev.danielarrais.votingsystem.infra.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESULTADOS")
public class ResultadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer votosFavoraveis;

    @Column(nullable = false)
    private Integer votosContrarios;

    @Column(nullable = false)
    private String resultado;

    @OneToOne
    private PautaEntity pauta;
}
