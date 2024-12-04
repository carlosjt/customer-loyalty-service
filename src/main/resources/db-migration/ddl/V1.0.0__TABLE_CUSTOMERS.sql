CREATE TABLE t_customers (
                           id SERIAL PRIMARY KEY,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           document_number VARCHAR(20) NOT NULL UNIQUE,
                           document_type VARCHAR(10) NOT NULL,
                           nationality VARCHAR(50),
                           email VARCHAR(100) UNIQUE,
                           phone VARCHAR(20),
                           birth_date DATE,
                           location VARCHAR(100) NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add CHECK constraints for additional validations and data consistency
ALTER TABLE t_customers
    ADD CONSTRAINT chk_document_type CHECK (document_type IN ('CI', 'RUC', 'PASSPORT')),
ADD CONSTRAINT chk_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
ADD CONSTRAINT chk_phone CHECK (phone ~ '^[0-9+\-() ]*$');

-- Trigger to automatically update the updated_at field
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_updated_at
    BEFORE INSERT OR UPDATE ON t_customers
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
