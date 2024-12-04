CREATE TABLE t_customer_segments (
                                     id SERIAL PRIMARY KEY,                          -- Identificador único de la segmentación
                                     segment_name VARCHAR(100) NOT NULL,             -- Nombre del segmento, e.g., "Frecuentes"
                                     description TEXT,                               -- Descripción del segmento
                                     age_range TEXT,                                 -- Rango de edad, e.g., "18-25"
                                     location VARCHAR(100),                          -- Ubicación específica, e.g., "Asunción"
                                     purchase_history_criteria TEXT,                 -- Criterios sobre historial de compras, e.g., "mes:100000:3:compra"
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha de creación
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Fecha de actualización
);
-- Trigger to update the updated_at field automatically
CREATE OR REPLACE FUNCTION update_segments_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_segments_updated_at
    BEFORE UPDATE ON t_customer_segments
    FOR EACH ROW
    EXECUTE FUNCTION update_segments_updated_at();
