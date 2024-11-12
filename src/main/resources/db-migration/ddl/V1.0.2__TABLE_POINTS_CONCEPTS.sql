CREATE TABLE t_points_concepts (
                                 id SERIAL PRIMARY KEY,
                                 concept_description VARCHAR(100) NOT NULL,
                                 required_points INT NOT NULL CHECK (required_points > 0),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger to automatically update the updated_at column
CREATE OR REPLACE FUNCTION update_points_concepts_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_points_concepts_updated_at
    BEFORE INSERT OR UPDATE ON t_points_concepts
    FOR EACH ROW
    EXECUTE FUNCTION update_points_concepts_updated_at_column();
