CREATE TABLE t_benefits (
                            id SERIAL PRIMARY KEY,
                            benefit_description TEXT UNIQUE NOT NULL,      -- Garantiza que los beneficios sean únicos
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Trigger to update the updated_at field automatically
CREATE OR REPLACE FUNCTION update_benefits_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_benefits_updated_at
    BEFORE UPDATE ON t_benefits
    FOR EACH ROW
    EXECUTE FUNCTION update_benefits_updated_at();
