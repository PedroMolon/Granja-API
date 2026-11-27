CREATE TABLE tb_user
(
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    senha     VARCHAR(255) NOT NULL, -- hash BCrypt
    perfil    VARCHAR(20)  NOT NULL DEFAULT 'ADMIN',
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT ck_user_perfil CHECK (perfil IN ('ADMIN'))
);

CREATE TABLE tb_empresa
(
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL UNIQUE,
    documento VARCHAR(20),
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE tb_standard
(
    id            BIGSERIAL PRIMARY KEY,
    empresa_id    BIGINT       NOT NULL,
    nome          VARCHAR(255) NOT NULL, -- ex.: Lohmann Brown-Lite
    versao_manual VARCHAR(50),
    observacoes   TEXT,
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT fk_standard_empresa FOREIGN KEY (empresa_id) REFERENCES tb_empresa (id),
    CONSTRAINT uq_standard_empresa_nome UNIQUE (empresa_id, nome)
);

CREATE TABLE tb_metrica
(
    id                     BIGSERIAL PRIMARY KEY,
    standard_id            BIGINT        NOT NULL,
    semana_idade           INT           NOT NULL,
    peso_medio_esperado_g  NUMERIC(8, 1) NOT NULL,
    peso_minimo_esperado_g NUMERIC(8, 1),
    peso_maximo_esperado_g NUMERIC(8, 1),
    -- opcionais, reservados para fases futuras
    consumo_racao_g_dia    NUMERIC(6, 1),
    taxa_postura_pct       NUMERIC(5, 1),
    peso_ovo_g             NUMERIC(5, 1),
    CONSTRAINT fk_metrica_standard FOREIGN KEY (standard_id) REFERENCES tb_standard (id),
    CONSTRAINT uq_metrica_standard_semana UNIQUE (standard_id, semana_idade),
    CONSTRAINT ck_metrica_semana CHECK (semana_idade >= 1),
    CONSTRAINT ck_metrica_peso CHECK (peso_medio_esperado_g > 0),
    CONSTRAINT ck_metrica_faixa CHECK (
        (peso_minimo_esperado_g IS NULL OR peso_minimo_esperado_g <= peso_medio_esperado_g) AND
        (peso_maximo_esperado_g IS NULL OR peso_maximo_esperado_g >= peso_medio_esperado_g)
        )
);

CREATE TABLE tb_lote
(
    id                BIGSERIAL PRIMARY KEY,
    codigo            VARCHAR(50) NOT NULL UNIQUE,
    standard_id       BIGINT      NOT NULL,
    data_alojamento   DATE        NOT NULL, -- pintainhas de 1 dia = idade 0
    qtd_inicial       INT         NOT NULL,
    galpao            VARCHAR(100),
    status            VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    data_encerramento DATE,
    observacoes       TEXT,
    criado_em         TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_lote_standard FOREIGN KEY (standard_id) REFERENCES tb_standard (id),
    CONSTRAINT ck_lote_qtd CHECK (qtd_inicial > 0),
    CONSTRAINT ck_lote_status CHECK (status IN ('ATIVO', 'ENCERRADO')),
    CONSTRAINT ck_lote_encerramento CHECK (
        (status = 'ATIVO' AND data_encerramento IS NULL) OR
        (status = 'ENCERRADO' AND data_encerramento IS NOT NULL
            AND data_encerramento >= data_alojamento)
        )
);

CREATE TABLE tb_registro_pesagem
(
    id                BIGSERIAL PRIMARY KEY,
    lote_id           BIGINT        NOT NULL,
    data_registro     DATE          NOT NULL,
    semana_idade      INT           NOT NULL, -- calculada pelo service
    peso_medio_real_g NUMERIC(8, 1) NOT NULL,
    qtd_aves_pesadas  INT,                    -- tamanho da amostra (opcional)
    observacoes       TEXT,
    criado_em         TIMESTAMPTZ   NOT NULL DEFAULT now(),
    atualizado_em     TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT fk_pesagem_lote FOREIGN KEY (lote_id) REFERENCES tb_lote (id),
    CONSTRAINT uq_pesagem_lote_data UNIQUE (lote_id, data_registro),
    CONSTRAINT ck_pesagem_semana CHECK (semana_idade >= 0),
    CONSTRAINT ck_pesagem_peso CHECK (peso_medio_real_g > 0),
    CONSTRAINT ck_pesagem_amostra CHECK (qtd_aves_pesadas IS NULL OR qtd_aves_pesadas > 0)
);

CREATE TABLE tb_registro_mortalidade
(
    id            BIGSERIAL PRIMARY KEY,
    lote_id       BIGINT      NOT NULL,
    data_registro DATE        NOT NULL,
    quantidade    INT         NOT NULL,
    observacoes   TEXT,
    criado_em     TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_mortalidade_lote FOREIGN KEY (lote_id) REFERENCES tb_lote (id),
    CONSTRAINT ck_mortalidade_qtd CHECK (quantidade > 0)
);

CREATE INDEX idx_standard_empresa ON tb_standard (empresa_id);
CREATE INDEX idx_lote_standard ON tb_lote (standard_id);
CREATE INDEX idx_lote_status ON tb_lote (status);
CREATE INDEX idx_pesagem_lote_semana ON tb_registro_pesagem (lote_id, semana_idade);
CREATE INDEX idx_mortalidade_lote_data ON tb_registro_mortalidade (lote_id, data_registro);