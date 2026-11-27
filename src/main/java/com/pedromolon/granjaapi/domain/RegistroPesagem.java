package com.pedromolon.granjaapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tb_registro_pesagem")
@Getter
@Setter
@NoArgsConstructor
public class RegistroPesagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "semana_idade", nullable = false)
    private Integer semanaIdade; // calculada pelo service, nunca digitada

    @Column(name = "peso_medio_real_g", nullable = false, precision = 8, scale = 1)
    private BigDecimal pesoMedioRealG;

    @Column(name = "qtd_aves_pesadas") private Integer qtdAvesPesadas;
    @Column(columnDefinition = "TEXT") private String observacoes;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    @PrePersist void prePersist() { criadoEm = atualizadoEm = Instant.now(); }
    @PreUpdate
    void preUpdate()  { atualizadoEm = Instant.now(); }
}
