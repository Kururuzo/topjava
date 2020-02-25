DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id)
VALUES (TIMESTAMP '2020-01-30 10:00:00', 'Завтрак ROLE_USER', 500, 100000),
       (TIMESTAMP '2020-01-30 13:00:00', 'Обед ROLE_USER', 1000, 100000),
       (TIMESTAMP '2020-01-30 20:00:00', 'Ужин ROLE_USER', 500, 100000),
       (TIMESTAMP '2020-01-31 00:00:00', 'Еда на граничное значение ROLE_USER', 100, 100000),
       (TIMESTAMP '2020-01-31 10:00:00', 'Завтрак ROLE_USER', 1000, 100000),
       (TIMESTAMP '2020-01-31 13:00:00', 'Обед ROLE_USER', 500, 100000),
       (TIMESTAMP '2020-01-31 20:00:00', 'Ужин ROLE_USER', 410, 100000);

--        (TIMESTAMP '2020-01-30 10:00:00', 'Завтрак ROLE_ADMIN', 500, 100001),
--        (TIMESTAMP '2020-01-30 13:00:00', 'Обед ROLE_ADMIN', 1000, 100001),
--        (TIMESTAMP '2020-01-30 20:00:00', 'Ужин ROLE_ADMIN', 500, 100001),
--        (TIMESTAMP '2020-01-31 00:00:00', 'Еда на граничное значение ROLE_ADMIN', 100, 100001),
--        (TIMESTAMP '2020-01-31 10:00:00', 'Завтрак ROLE_ADMIN', 1000, 100001),
--        (TIMESTAMP '2020-01-31 13:00:00', 'Обед ROLE_ADMIN', 500, 100001),
--        (TIMESTAMP '2020-01-31 20:00:00', 'Ужин ROLE_ADMIN', 410, 100001);
