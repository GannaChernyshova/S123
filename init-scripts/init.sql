CREATE TABLE IF NOT EXISTS todos
(
    id           varchar(100)    NOT NULL,
    title        varchar(200)    NOT NULL,
    completed    boolean         DEFAULT FALSE,
    order_number int,
    PRIMARY KEY (id)
);

INSERT INTO todos (id, title, completed, order_number)
VALUES ('1', 'First Todo', false, 1),
       ('2', 'Second Todo', false, 2),
       ('3', 'Completed Todo', true, 3) ON CONFLICT (id) DO NOTHING;