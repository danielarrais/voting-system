package dev.danielarrais.votingsystem.infra.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PAUTAS")
public class PautaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @ManyToOne
    private AssembleiaEntity assembleia;

    @OneToOne
    private SessaoEntity sessao;
}