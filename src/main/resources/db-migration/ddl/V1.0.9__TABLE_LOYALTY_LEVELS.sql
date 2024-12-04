CREATE TABLE t_loyalty_levels (
                                  id SERIAL PRIMARY KEY,                           -- Identificador único del nivel
                                  level_name VARCHAR(50) UNIQUE NOT NULL,          -- Nombre del nivel (e.g., "Bronze", "Silver", "Gold")
                                  min_points INT NOT NULL CHECK (min_points >= 0), -- Puntos mínimos para alcanzar este nivel
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Trigger to update the updated_at field automatically
CREATE OR REPLACE FUNCTION update_loyalty_levels_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_loyalty_levels_updated_at
    BEFORE UPDATE ON t_loyalty_levels
    FOR EACH ROW
    EXECUTE FUNCTION update_loyalty_levels_updated_at();
