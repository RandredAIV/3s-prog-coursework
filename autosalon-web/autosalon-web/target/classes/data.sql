TRUNCATE TABLE cars CASCADE;
TRUNCATE TABLE teams CASCADE;

-- Команды
INSERT INTO teams (id, name, country)
VALUES
    (1, 'Red Bull Racing', 'Австрия'),
    (2, 'Mercedes AMG F1', 'Германия'),
    (3, 'Ferrari', 'Италия');

-- Болиды
INSERT INTO cars (id, name, price, team_id)
VALUES
    (1, 'RB19', 12000000, 1),
    (2, 'RB20', 13000000, 1),
    (3, 'W14', 11500000, 2),
    (4, 'SF-23', 11800000, 3);
