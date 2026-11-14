--liquibase formatted sql

--changeset phyllosedis:3
CREATE TABLE IF NOT EXISTS scripts (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    sandbox_id UUID NOT NULL REFERENCES sandbox_configs(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    source_code TEXT NOT NULL,
    input_schema JSONB NOT NULL DEFAULT '[]',
    version INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (company_id, name, version)
);
