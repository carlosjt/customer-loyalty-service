CREATE TABLE t_payment_transactions (
                                        id SERIAL PRIMARY KEY,
                                        transaction_id VARCHAR(100) NOT NULL,         -- ID único de la transacción (generado por la plataforma)
                                        customer_id INT NOT NULL,                     -- Cliente asociado a la transacción
                                        payment_amount NUMERIC(12, 2) NOT NULL,       -- Monto pagado
                                        platform VARCHAR(50) NOT NULL,                -- Plataforma utilizada (e.g., Stripe, PayPal)
                                        status VARCHAR(20) NOT NULL,                  -- Estado de la transacción (e.g., 'Completed')
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (customer_id) REFERENCES t_customers(id) ON DELETE CASCADE
);

-- Trigger Function to update the updated_at field
CREATE OR REPLACE FUNCTION update_payment_transactions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_payment_transactions_updated_at
    BEFORE UPDATE ON t_payment_transactions
    FOR EACH ROW
    EXECUTE FUNCTION update_payment_transactions_updated_at();
