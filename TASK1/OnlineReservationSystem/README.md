
# 🚆 Train Reservation System (Java + MySQL)

This project is a **Java-based Train Reservation System** built using **Java Swing** for the GUI and **MySQL** as the backend database. It allows users to book, view, and manage train tickets through an intuitive desktop application interface.

---

## 📁 Project Structure

```
OIBSIP/
└── TASK1/
    └── OnlineReservationSystem/
        ├── src/
        │   └── TrainReservationSystem.java
        ├── sql/
        │   └── train_reservation.sql
        ├── lib/
        │   └── mysql-connector-j-9.3.0.jar
        └── .gitignore
```

---

## ✅ Features

- 📝 Passenger registration and input form  
- 🚉 Train selection and booking  
- 💾 Stores reservation details in MySQL database  
- 🔍 View reservations  
- ❌ Cancel bookings  
- 🎨 User-friendly interface (Java Swing)  

---

## 🧑‍💻 Tech Stack

| Technology     | Description                  |
|----------------|------------------------------|
| Java (Swing)   | GUI development               |
| MySQL          | Database management           |
| JDBC           | Java Database Connectivity    |
| VS Code        | Development environment       |

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/OIBSIP.git
cd OIBSIP/TASK1/OnlineReservationSystem
```

### 2. Import SQL Schema

- Open **MySQL Workbench** or **phpMyAdmin**
- Import the `sql/train_reservation.sql` file into your MySQL server.

### 3. Update Database Credentials

Edit the `TrainReservationSystem.java` file:
```java
String url = "jdbc:mysql://localhost:3306/train_reservation";
String username = "your_mysql_username";
String password = "your_mysql_password";
```

### 4. Add MySQL JDBC Driver

Ensure the MySQL JDBC JAR (`mysql-connector-j-9.3.0.jar`) is added to your project classpath.

### 5. Run the Application

Compile and run `TrainReservationSystem.java` in your IDE or terminal.

---

## 📸 Screenshots

> *(Optional)* Add screenshots of your UI here to showcase how the system looks.

---

## 📌 License

This project is open-source and available under the [MIT License](LICENSE).

---

## 👨‍💻 Author

- **Satya Prakash** — [GitHub](https://github.com/SatyaPrakash252)

---

## 🌟 Acknowledgements

- Oasis Infobyte Internship  
- MySQL Documentation  
- Java Swing API  
