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
    BEFORE INSERT OR UPDATE ON t_points_expiration
    FOR EACH ROW
    EXECUTE FUNCTION update_points_expiration_updated_at_column();

-- Check if there are any records that overlap with the new date range
CREATE OR REPLACE FUNCTION check_no_overlap_dates()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM t_points_expiration
        WHERE id <> NEW.id  -- Ignorar el registro actual en caso de actualización
          AND daterange(start_date, COALESCE(end_date, 'infinity'::date), '[]') &&
              daterange(NEW.start_date, COALESCE(NEW.end_date, 'infinity'::date), '[]')
    ) THEN
        RAISE EXCEPTION 'El rango de fechas se solapa con otro registro existente';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_no_overlap_dates
    BEFORE INSERT OR UPDATE ON t_points_expiration
    FOR EACH ROW
    EXECUTE FUNCTION check_no_overlap_dates();

