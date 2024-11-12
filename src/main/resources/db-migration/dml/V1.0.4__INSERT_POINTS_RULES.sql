INSERT INTO t_points_rules (lower_limit, upper_limit, equivalence_amount, points_awarded)
VALUES
    (0, 199999, 50000, 1),       -- 1 punto cada 50,000 Gs. para consumos entre 0 y 199,999 Gs.
    (200000, 499999, 30000, 1),  -- 1 punto cada 30,000 Gs. para consumos entre 200,000 y 499,999 Gs.
    (500000, NULL, 20000, 1);    -- 1 punto cada 20,000 Gs. para consumos de 500,000 Gs. en adelante.
