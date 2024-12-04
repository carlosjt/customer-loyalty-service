CREATE TABLE t_loyalty_level_benefits (
                                          id SERIAL PRIMARY KEY,                                                           -- Identificador único de la relación
                                          loyalty_level_id INT NOT NULL REFERENCES t_loyalty_levels(id) ON DELETE CASCADE, -- Nivel de lealtad asociado
                                          benefit_id INT NOT NULL REFERENCES t_benefits(id) ON DELETE CASCADE,             -- Beneficio asociado
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Trigger to automatically update the updated_at field
CREATE OR REPLACE FUNCTION update_loyalty_level_benefits_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_loyalty_level_benefits_updated_at
    BEFORE UPDATE ON t_loyalty_level_benefits
    FOR EACH ROW
    EXECUTE FUNCTION update_loyalty_level_benefits_updated_at();
