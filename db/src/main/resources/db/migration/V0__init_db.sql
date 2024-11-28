CREATE TABLE notes
(
    id         UUID         not null PRIMARY KEY,
    note       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

CREATE TABLE events
(
    id         UUID         NOT NULL PRIMARY KEY,
    event      VARCHAR(255) NOT NULL,
    data       TEXT         NULL,
    created_at TIMESTAMP    NOT NULL
);