CREATE TABLE notes
(
    id         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    note       VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)