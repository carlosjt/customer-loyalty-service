CREATE TABLE t_points_usage_header (
                                       id SERIAL PRIMARY KEY,
                                       customer_id BIGINT NOT NULL REFERENCES t_customers(id) ON DELETE CASCADE,              -- Identificador del cliente
                                       total_points_used INT NOT NULL CHECK (total_points_used > 0),                          -- Total de puntos utilizados en esta transacción
                                       usage_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                                        -- Fecha de uso de puntos
                                       usage_concept_id BIGINT NOT NULL REFERENCES t_points_concepts(id) ON DELETE SET NULL,  -- Concepto de uso de punto
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger to automatically update the updated_at column
CREATE OR REPLACE FUNCTION update_t_points_usage_header_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_t_points_usage_header_updated_at
    BEFORE INSERT OR UPDATE ON t_points_usage_header
    FOR EACH ROW
    EXECUTE FUNCTION update_t_points_usage_header_updated_at_column();
