package com.pedromolon.granjaapi.domain;

import com.pedromolon.granjaapi.domain.enums.StatusLote;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tb_lote")
@Getter
@Setter
@NoArgsConstructor
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "standard_id", nullable = false)
    private Standard standard;

    @Column(name = "data_alojamento", nullable = false)
    private LocalDate dataAlojamento;

    @Column(name = "qtd_inicial", nullable = false)
    private Integer qtdInicial;

    @Column(length = 100) private String galpao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusLote status = StatusLote.ATIVO;

    @Column(name = "data_encerramento") private LocalDate dataEncerramento;
    @Column(columnDefinition = "TEXT") private String observacoes;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist void prePersist() { criadoEm = Instant.now(); }
}
