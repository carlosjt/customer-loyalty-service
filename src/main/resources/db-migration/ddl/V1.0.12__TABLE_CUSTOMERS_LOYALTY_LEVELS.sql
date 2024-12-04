CREATE TABLE t_customer_loyalty (
                                    id SERIAL PRIMARY KEY,
                                    customer_id INT NOT NULL,                 -- ID del cliente (FK a t_customers)
                                    loyalty_level_id INT NOT NULL,            -- ID del nivel de fidelización (FK a t_loyalty_levels)
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (customer_id) REFERENCES t_customers(id) ON DELETE CASCADE,
                                    FOREIGN KEY (loyalty_level_id) REFERENCES t_loyalty_levels(id) ON DELETE CASCADE
);
-- Trigger to update the updated_at field automatically
CREATE OR REPLACE FUNCTION update_customer_loyalty_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_customer_loyalty_updated_at
    BEFORE UPDATE ON t_customer_loyalty
    FOR EACH ROW
    EXECUTE FUNCTION update_customer_loyalty_updated_at();

