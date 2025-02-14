INSERT INTO role (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN'),
    ('ROLE_MANAGER');

INSERT INTO users (first_name, last_name, password, email, birthday)
VALUES
    ('Vlad', 'Panasik', '$2a$10$8zkq1eo01zo94S.eTWa4qOaYMx.tFBFI1SY5Nl7G/FhuuqthXIcUO', 'vlad@gmail.com', CURRENT_DATE - INTERVAL '2 years'),
    ('John', 'Doe', '$2a$10$8zkq1eo01zo94S.eTWa4qOaYMx.tFBFI1SY5Nl7G/FhuuqthXIcUO', 'john.doe@example.com', '1990-05-15'),
    ('Jane', 'Smith', '$2a$10$8zkq1eo01zo94S.eTWa4qOaYMx.tFBFI1SY5Nl7G/FhuuqthXIcUO', 'jane.smith@example.com', '1985-08-22');

INSERT INTO user_role
VALUES
    (1,1),
    (2,1),
    (3,1)