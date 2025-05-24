-- Create the database
CREATE DATABASE IF NOT EXISTS OnlineExamDB;
USE OnlineExamDB;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Create questions table
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    option1 VARCHAR(255) NOT NULL,
    option2 VARCHAR(255) NOT NULL,
    option3 VARCHAR(255) NOT NULL,
    option4 VARCHAR(255) NOT NULL,
    correct_option INT NOT NULL CHECK (correct_option BETWEEN 1 AND 4)
);

INSERT INTO questions (question, option1, option2, option3, option4, correct_option) VALUES
('Which feature was introduced in Java 8?', 'Lambda Expressions', 'Serialization', 'AWT', 'Threads', 1),
('Which is not a core Hibernate interface?', 'Session', 'Transaction', 'SessionFactory', 'JDBCManager', 4),
('Which keyword is used to inherit a class in Java?', 'super', 'this', 'extends', 'implements', 3),
('Which method is used to start a thread in Java?', 'start()', 'run()', 'execute()', 'init()', 1),
('What is JVM?', 'Java Virtual Machine', 'Java Volume Manager', 'Java Visual Machine', 'None', 1),
('Which loop is guaranteed to execute at least once?', 'for', 'while', 'do-while', 'none', 3),
('What is a correct syntax to output "Hello World" in Java?', 'echo("Hello World");', 'Console.WriteLine("Hello World");', 'System.out.println("Hello World");', 'print("Hello World");', 3),
('Which Java keyword is used to define a constant variable?', 'static', 'final', 'const', 'define', 2),
('Which company developed Java?', 'Sun Microsystems', 'Oracle', 'Microsoft', 'Google', 1),
('Which of these is not a Java feature?', 'Object-oriented', 'Use of pointers', 'Portable', 'Dynamic and Extensible', 2);
