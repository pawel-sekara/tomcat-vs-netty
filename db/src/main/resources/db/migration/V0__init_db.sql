CREATE TABLE notes
(
    id         UUID         not null PRIMARY KEY,
    note       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL
);