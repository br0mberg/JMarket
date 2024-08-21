CREATE TABLE PERSON {
    id int PRIMARY KEY,
    name varchar(30) NOT NULL,
    age int NOT NULL CHECK (age >= 0),
    email varchar(100) NOT NULL UNIQUE,
    role varchar NOT NULL,
    date_of_birth DATE NOT NULL,
    registration_date TIMESTAMP NOT NULL
};

CREATE TABLE Item {
    id int PRIMARY KEY,
    name varchar(50) NOT NULL,
    description varchar(255) NOT NULL,
    category varchar NOT NULL,
    price int NOT NULL CHECK (price >= 0),
    quantity int NOT NULL CHECK (quantity >= 0),
    article_number varchar(12) NOT NULL UNIQUE,
    person_id int REFERENCES Person(id) ON DELETE SET NULL,
    created_date TIMESTAMP NOT NULL,
    quantity_change_date TIMESTAMP NOT NULL
};