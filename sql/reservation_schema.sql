CREATE DATABASE IF NOT EXISTS reservationdb;
USE reservationdb;

CREATE TABLE IF NOT EXISTS reservations (
    pnr_no INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    gender VARCHAR(10),
    train_no VARCHAR(20),
    class_type VARCHAR(20),
    journey_date DATE,
    source VARCHAR(50),
    destination VARCHAR(50)
);
