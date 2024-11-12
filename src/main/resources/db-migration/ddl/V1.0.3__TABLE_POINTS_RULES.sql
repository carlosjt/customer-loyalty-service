CREATE TABLE t_points_rules (
                              id SERIAL PRIMARY KEY,
                              lower_limit NUMERIC(12, 2) CHECK (lower_limit >= 0),
                              upper_limit NUMERIC(12, 2) CHECK (upper_limit >= lower_limit),
                              equivalence_amount NUMERIC(12, 2) NOT NULL CHECK (equivalence_amount > 0),
                              points_awarded INT NOT NULL CHECK (points_awarded > 0),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger to automatically update the updated_at column
CREATE OR REPLACE FUNCTION update_points_rules_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_points_rules_updated_at
    BEFORE INSERT OR UPDATE ON t_points_rules
    FOR EACH ROW
    EXECUTE FUNCTION update_points_rules_updated_at_column();
