-- =====================================================
-- Схема БД для Admin Service (admin_db)
-- Контракт для хранения заявок на отчеты и их статусов
-- =====================================================

-- Таблица отчетов
CREATE TABLE reports
(
    id                        UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    date_from                 DATE        NOT NULL,
    date_to                   DATE        NOT NULL,
    report_type               VARCHAR(50) NOT NULL CHECK (report_type IN ('USER_ACTIVITY', 'SALES_ANALYSIS', 'SYSTEM_METRICS')),
    status                    VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED')),
    priority                  VARCHAR(20) NOT NULL DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    description               TEXT,
    progress                  INTEGER              DEFAULT 0 CHECK (progress >= 0 AND progress <= 100),
    estimated_completion_time TIMESTAMP,
    error_message             TEXT,
    created_at                TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by                VARCHAR(255)
);

-- Таблица для хранения метаданных готовых отчетов
CREATE TABLE report_files
(
    id                      UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    report_id               UUID         NOT NULL REFERENCES reports (id) ON DELETE CASCADE,
    file_path               VARCHAR(500) NOT NULL,
    file_size               BIGINT       NOT NULL,
    row_count               INTEGER      NOT NULL,
    format                  VARCHAR(10)  NOT NULL CHECK (format IN ('csv', 'excel', 'pdf')),
    completed_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processing_time_seconds INTEGER,
    data_sources            TEXT[] -- Массив источников данных
);

-- Таблица для аудита изменений статусов
CREATE TABLE report_status_history
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    report_id  UUID        NOT NULL REFERENCES reports (id) ON DELETE CASCADE,
    old_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    changed_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(255),
    reason     TEXT
);

-- =====================================================
-- Схема БД для Report Generator Service (report_db)
-- Контракт для хранения исходных данных и результатов обработки
-- =====================================================

-- Таблица для хранения задач на генерацию
CREATE TABLE generation_tasks
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    report_id     UUID        NOT NULL, -- Ссылка на отчет в admin_db
    date_from     DATE        NOT NULL,
    date_to       DATE        NOT NULL,
    report_type   VARCHAR(50) NOT NULL,
    priority      VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED')),
    worker_id     VARCHAR(100),
    started_at    TIMESTAMP,
    completed_at  TIMESTAMP,
    error_code    VARCHAR(100),
    error_message TEXT,
    retry_count   INTEGER              DEFAULT 0,
    max_retries   INTEGER              DEFAULT 3,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для хранения прогресса обработки
CREATE TABLE progress_updates
(
    id                       UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    task_id                  UUID      NOT NULL REFERENCES generation_tasks (id) ON DELETE CASCADE,
    progress                 INTEGER   NOT NULL CHECK (progress >= 0 AND progress <= 100),
    current_step             VARCHAR(200),
    estimated_time_remaining INTEGER, -- в секундах
    updated_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для хранения результатов обработки
CREATE TABLE generation_results
(
    id                      UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    task_id                 UUID         NOT NULL REFERENCES generation_tasks (id) ON DELETE CASCADE,
    file_path               VARCHAR(500) NOT NULL,
    file_size               BIGINT       NOT NULL,
    row_count               INTEGER      NOT NULL,
    processing_time_seconds INTEGER,
    data_sources            TEXT[],
    created_at              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);