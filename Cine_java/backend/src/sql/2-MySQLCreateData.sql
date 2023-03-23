-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "paproject" database.
-------------------------------------------------------------------------------

-- Users -- password=pa2223
INSERT INTO User(id, userName, password, firstName, lastName, email, role) VALUES
    (1, 'viewer', '$2a$10$8o34vbwlRURkBGETvQzr8OCuPrk52E.j2ilm4KGKPrwNR89eNV/YG', 'paca', 'jimenez', 'TuPakita@udc.es', 'VIEWER'),
    (2, 'ticketseller', '$2a$10$8o34vbwlRURkBGETvQzr8OCuPrk52E.j2ilm4KGKPrwNR89eNV/YG', 'paco', 'jimenez', 'TuPakitofav@udc.es', 'TICKET_SELLER');

-- Salas
INSERT INTO Hall(id, name, capacity) VALUES
    (1, 'sala1', 9),
    (2, 'sala2', 150);

-- Peliculas
INSERT INTO Film(id, title, summary, duration) VALUES
    (1, 'Titanic', 'se muere 1 pero cabian los 2', 125),
    (2, 'peter pan', 'el niño este que no envejece', 30);

-- Sesiones -- HOY
INSERT INTO Session(id, dateTime, price, freeLocs, hallId, filmId) VALUES
    (1, DATE_ADD(DATE(NOW()), INTERVAL '0 00:05' DAY_MINUTE), 5, 9, 1, 1),
    (2, DATE_ADD(DATE(NOW()), INTERVAL '0 23:55' DAY_MINUTE), 10, 150, 2, 2);

-- Sesiones -- 6 dias siguientes
INSERT INTO Session(dateTime, price, freeLocs, hallId, filmId) VALUES
    (DATE_ADD(DATE(NOW()), INTERVAL '1 00:05' DAY_MINUTE), 5, 5, 1, 1),
    (DATE_ADD(DATE(NOW()), INTERVAL '1 23:55' DAY_MINUTE), 10, 150, 2, 2),
    (DATE_ADD(DATE(NOW()), INTERVAL '2 00:05' DAY_MINUTE), 5, 9, 1, 1),
    (DATE_ADD(DATE(NOW()), INTERVAL '2 23:55' DAY_MINUTE), 10, 150, 2, 2),
    (DATE_ADD(DATE(NOW()), INTERVAL '3 00:05' DAY_MINUTE), 5, 9, 1, 1),
    (DATE_ADD(DATE(NOW()), INTERVAL '3 23:55' DAY_MINUTE), 10, 150, 2, 2),
    (DATE_ADD(DATE(NOW()), INTERVAL '4 00:05' DAY_MINUTE), 5, 9, 1, 1),
    (DATE_ADD(DATE(NOW()), INTERVAL '4 23:55' DAY_MINUTE), 10, 150, 2, 2),
    (DATE_ADD(DATE(NOW()), INTERVAL '5 00:05' DAY_MINUTE), 5, 9, 1, 1),
    (DATE_ADD(DATE(NOW()), INTERVAL '5 23:55' DAY_MINUTE), 10, 150, 2, 2),
    (DATE_ADD(DATE(NOW()), INTERVAL '6 00:05' DAY_MINUTE), 5, 9, 1, 1),
    (DATE_ADD(DATE(NOW()), INTERVAL '6 23:55' DAY_MINUTE), 10, 150, 2, 2);

-- Compras
INSERT INTO Purchase(id, amount, creditCard, dateTime, delivered, sessionId, userId) VALUES
    (1,1,"1234123412341234",DATE(NOW()),FALSE,1,1),
    (2,3,"1234123412341234",DATE(NOW()),FALSE,1,1);

