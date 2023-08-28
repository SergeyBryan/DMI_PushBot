create TABLE model (
    model_code INT PRIMARY KEY,
    material INT,
    model_name VARCHAR(255),
    qty INT
);
create TABLE app_users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_date TIMESTAMP,
    modified_time TIMESTAMP,
    email VARCHAR(255),
    status INT,
    chat_id BIGINT,
    store_number VARCHAR(255)
);
create TABLE request (
    id SERIAL PRIMARY KEY,
    model_code INT,
    qty INT,
    comment VARCHAR(255),
    size VARCHAR(255),
    created_date TIMESTAMP,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_users (id)
);