CREATE TABLE t_points_expiration (
                                   id SERIAL PRIMARY KEY,
                                   start_date DATE NOT NULL,
                                   end_date DATE,
                                   duration_days INT CHECK (duration_days > 0),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   CHECK (end_date IS NOT NULL OR duration_days IS NOT NULL)
);

-- Trigger to automatically update the updated_at column
CREATE OR REPLACE FUNCTION update_points_expiration_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_points_expiration_updated_at
    BEFORE UPDATE ON t_points_expiration
    FOR EACH ROW
    EXECUTE FUNCTION update_points_expiration_updated_at_column();
