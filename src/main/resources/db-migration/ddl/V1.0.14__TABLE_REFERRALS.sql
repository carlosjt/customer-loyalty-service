CREATE TABLE t_referrals (
                             id SERIAL PRIMARY KEY,
                             customer_id INT NOT NULL,                                     -- ID del cliente que refiere
                             referred_email VARCHAR(100) NOT NULL UNIQUE,                  -- Email del cliente referido (se completa tras el registro)
                             referral_status VARCHAR(20) DEFAULT 'PENDING',                -- Estado de la referencia
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (customer_id) REFERENCES t_customers(id) ON DELETE CASCADE
);

-- Add CHECK restriction to referral_status field
ALTER TABLE t_referrals
    ADD CONSTRAINT chk_referral_status
        CHECK (referral_status IN ('PENDING', 'COMPLETED', 'CANCELLED'));

-- Trigger Function to update the updated_at field
CREATE OR REPLACE FUNCTION update_referrals_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_referrals_updated_at
    BEFORE UPDATE ON t_referrals
    FOR EACH ROW
    EXECUTE FUNCTION update_referrals_updated_at();
