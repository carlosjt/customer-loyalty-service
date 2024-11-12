CREATE TABLE t_points_usage_detail (
                                       id SERIAL PRIMARY KEY,
                                       usage_header_id BIGINT NOT NULL REFERENCES t_points_usage_header(id) ON DELETE CASCADE,  -- Referencia a la cabecera de uso
                                       points_used INT NOT NULL CHECK (points_used > 0),                                        -- Puntos utilizados de esta bolsa
                                       points_wallet_id BIGINT NOT NULL REFERENCES t_points_wallet(id) ON DELETE RESTRICT,      -- Referencia a la bolsa de puntos utilizada
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger to update the use of points in the corresponding bag
CREATE OR REPLACE FUNCTION update_t_points_wallet_used_points()
RETURNS TRIGGER AS $$
BEGIN
UPDATE t_points_wallet
SET used_points = used_points + NEW.points_used
WHERE id = NEW.points_wallet_id;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_t_points_wallet_used_points
    AFTER INSERT OR UPDATE ON t_points_usage_detail
    FOR EACH ROW
    EXECUTE FUNCTION update_t_points_wallet_used_points();

-- Trigger para actualizar automáticamente la columna update_at
CREATE OR REPLACE FUNCTION update_t_points_usage_detail_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_t_points_usage_detail_updated_at
    BEFORE INSERT OR UPDATE ON t_points_usage_detail
    FOR EACH ROW
    EXECUTE FUNCTION update_t_points_usage_detail_updated_at_column();
