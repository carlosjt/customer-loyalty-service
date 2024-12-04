CREATE TABLE t_points_wallet (
                                 id SERIAL PRIMARY KEY,
                                 customer_id BIGINT NOT NULL REFERENCES t_customers(id) ON DELETE CASCADE,       -- Referencia al cliente
                                 assignment_date DATE NOT NULL,                                                  -- Fecha de asignación de puntos
                                 expiration_id BIGINT REFERENCES t_points_expiration(id) ON DELETE SET NULL,     -- Referencia a la configuración de expiración de puntos
                                 rule_id BIGINT NOT NULL REFERENCES t_points_rules(id) ON DELETE RESTRICT,       -- Referencia a la regla aplicada para la asignación de puntos
                                 assigned_points INT NOT NULL CHECK (assigned_points >= 0),                      -- Puntos asignados
                                 used_points INT DEFAULT 0 CHECK (used_points >= 0),                             -- Puntos utilizados
                                 balance_points INT GENERATED ALWAYS AS (assigned_points - used_points) STORED,  -- Saldo calculado
                                 transaction_amount NUMERIC(12, 2) NOT NULL CHECK (transaction_amount >= 0),     -- Monto de la operación que generó los puntos
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger to automatically update the updated_at column
CREATE OR REPLACE FUNCTION update_t_points_wallet_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_t_points_wallet_updated_at
    BEFORE INSERT OR UPDATE ON t_points_wallet
    FOR EACH ROW
    EXECUTE FUNCTION update_t_points_wallet_updated_at_column();
