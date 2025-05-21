CREATE DATABASE train_reservation;

USE train_reservation;

CREATE TABLE users (
    username VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100)
);

CREATE TABLE trains (
    train_no INT PRIMARY KEY,
    train_name VARCHAR(100) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    fare INT NOT NULL
);

CREATE TABLE reservations (
    pnr BIGINT PRIMARY KEY,
    username VARCHAR(100),
    train_no INT,
    train_name VARCHAR(100),
    date DATE,
    source VARCHAR(100),
    destination VARCHAR(100),
    price INT
);
