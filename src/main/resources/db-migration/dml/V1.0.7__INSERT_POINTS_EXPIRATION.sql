INSERT INTO t_points_expiration (start_date, end_date, duration_days)
VALUES
    ('2024-01-01', '2024-12-31', 365),     -- Validez de un año completo desde el 1 de enero hasta el 31 de diciembre de 2024.
    ('2024-01-01', '2024-06-29', 180),     -- Validez de 180 días a partir del 1 de enero de 2024, terminando el 29 de junio de 2024.
    ('2024-06-01', '2024-12-01', 184),     -- Validez de 184 días desde el 1 de junio hasta el 1 de diciembre de 2024.
    ('2024-08-01', '2024-10-30', 90);      -- Validez de 90 días a partir del 1 de agosto de 2024, terminando el 30 de octubre de 2024.
