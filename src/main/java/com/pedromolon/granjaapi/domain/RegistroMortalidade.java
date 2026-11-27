package com.pedromolon.granjaapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tb_registro_mortalidade")
@Getter
@Setter
@NoArgsConstructor
public class RegistroMortalidade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(nullable = false) private Integer quantidade;
    @Column(columnDefinition = "TEXT") private String observacoes;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist void prePersist() { criadoEm = Instant.now(); }
}
