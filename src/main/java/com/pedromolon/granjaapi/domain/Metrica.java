package com.pedromolon.granjaapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_metrica")
@Getter
@Setter
@NoArgsConstructor
public class Metrica {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "standard_id", nullable = false)
    private Standard standard;

    @Column(name = "semana_idade", nullable = false)
    private Integer semanaIdade;

    @Column(name = "peso_medio_esperado_g", nullable = false, precision = 8, scale = 1)
    private BigDecimal pesoMedioEsperadoG;

    @Column(name = "peso_minimo_esperado_g", precision = 8, scale = 1)
    private BigDecimal pesoMinimoEsperadoG;

    @Column(name = "peso_maximo_esperado_g", precision = 8, scale = 1)
    private BigDecimal pesoMaximoEsperadoG;

    // opcionais, reservados para fases futuras
    @Column(name = "consumo_racao_g_dia", precision = 6, scale = 1)
    private BigDecimal consumoRacaoGDia;

    @Column(name = "taxa_postura_pct", precision = 5, scale = 1)
    private BigDecimal taxaPosturaPct;

    @Column(name = "peso_ovo_g", precision = 5, scale = 1)
    private BigDecimal pesoOvoG;
}
