CREATE TABLE "user" (
    id int  PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY ,
    username varchar(30) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL,
    age int NOT NULL CHECK (age >= 0),
    email varchar(100) NOT NULL UNIQUE,
    date_of_birth date NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE Item (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price INT NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    article_number VARCHAR(12) NOT NULL UNIQUE,
    user_id INT REFERENCES "user"(id) ON DELETE SET NULL,
    created_date TIMESTAMP NOT NULL,
    quantity_change_date TIMESTAMP NOT NULL
);
