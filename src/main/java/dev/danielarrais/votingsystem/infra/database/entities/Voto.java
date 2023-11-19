package dev.danielarrais.votingsystem.infra.database.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VOTOS")
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String CPF;

    @Column(nullable = false)
    private LocalDateTime hora;

    @Column(nullable = false)
    private Boolean voto;

    @ManyToOne
    private Sessao sessao;
}
