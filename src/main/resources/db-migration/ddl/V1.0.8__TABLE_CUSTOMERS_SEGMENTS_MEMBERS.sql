CREATE TABLE t_customer_segment_members (
                                            id SERIAL PRIMARY KEY,                           -- Identificador único
                                            customer_id INT NOT NULL,                        -- ID del cliente (FK a t_customers)
                                            segment_id INT NOT NULL,                         -- ID del segmento (FK a t_customer_segments)
                                            assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha de asignación
                                            FOREIGN KEY (customer_id) REFERENCES t_customers(id) ON DELETE CASCADE,
                                            FOREIGN KEY (segment_id) REFERENCES t_customer_segments(id) ON DELETE CASCADE
);
