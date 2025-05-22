package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;

public class TrainReservationSystem {
    static Connection conn;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            connectDB();
            showLogin();
        });
    }

    static void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/train_reservation", "root", "1234abc");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed:\n" + e);
            System.exit(1);
        }
    }

    static void showLogin() {
        JFrame frame = createFrame("Login");
        JPanel panel = createCenteredPanel();

        JLabel heading = new JLabel("🚆 Welcome to Train Booking System 🚆", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(50, 54, 65));
        panel.add(heading);

        JTextField userField = createTextField();
        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 30));
        passField.setBackground(Color.WHITE);
        JButton login = createButton("Login");
        JButton register = createButton("Register");

        panel.add(createLabel("Username:"));
        panel.add(userField);
        panel.add(createLabel("Password:"));
        panel.add(passField);
        panel.add(login);
        panel.add(register);

        login.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter username and password.");
                return;
            }
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                ps.setString(1, user);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    frame.dispose();
                    showReservation(user);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Login Failed: " + ex.getMessage());
            }
        });

        register.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter username and password.");
                return;
            }
            try {
                PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE username=?");
                check.setString(1, user);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Username already exists!");
                } else {
                    PreparedStatement ps = conn
                            .prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
                    ps.setString(1, user);
                    ps.setString(2, pass);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "User Registered!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Registration Failed: " + ex.getMessage());
            }
        });

        JPanel wrapper = wrapCentered(panel);
        frame.getContentPane().add(wrapper, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    static void showReservation(String user) {
        JFrame frame = createFrame("Reservation");
        JPanel panel = createCenteredPanel();

        JTextField trainNoField = createTextField();
        JTextField trainNameField = createTextField();
        trainNameField.setEditable(false);
        JTextField priceField = createTextField();
        priceField.setEditable(false);
        JTextField totalField = createTextField();
        totalField.setEditable(false);
        JTextField dateField = createTextField();

        String[] cities = { "Delhi", "Mumbai", "Kolkata", "Chennai", "Bangalore", "Hyderabad" };
        JComboBox<String> sourceBox = new JComboBox<>(cities);
        JComboBox<String> destBox = new JComboBox<>(cities);

        JButton book = createButton("Book Ticket");
        JButton cancel = createButton("Cancel Ticket");

        panel.add(createLabel("Train Number:"));
        panel.add(trainNoField);
        panel.add(createLabel("Train Name:"));
        panel.add(trainNameField);
        panel.add(createLabel("Fare (INR):"));
        panel.add(priceField);
        panel.add(createLabel("Total Fare (1 seat):"));
        panel.add(totalField);
        panel.add(createLabel("Date of Journey (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(createLabel("From:"));
        panel.add(sourceBox);
        panel.add(createLabel("To:"));
        panel.add(destBox);
        panel.add(book);
        panel.add(cancel);

        trainNoField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    int tNo = Integer.parseInt(trainNoField.getText().trim());
                    PreparedStatement ps = conn
                            .prepareStatement("SELECT train_name, fare FROM trains WHERE train_no=?");
                    ps.setInt(1, tNo);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        trainNameField.setText(rs.getString("train_name"));
                        priceField.setText(String.valueOf(rs.getInt("fare")));
                        totalField.setText(priceField.getText());
                    } else {
                        trainNameField.setText("");
                        priceField.setText("");
                        totalField.setText("");
                    }
                } catch (NumberFormatException ignored) {
                    trainNameField.setText("");
                    priceField.setText("");
                    totalField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        book.addActionListener(e -> {
            try {
                long pnr = Math.abs(new Random().nextLong() % 1_000_000_0000L);
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO reservations (pnr, username, train_no, train_name, date, source, destination, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setLong(1, pnr);
                ps.setString(2, user);
                ps.setInt(3, Integer.parseInt(trainNoField.getText().trim()));
                ps.setString(4, trainNameField.getText());
                ps.setDate(5, Date.valueOf(dateField.getText().trim()));
                ps.setString(6, (String) sourceBox.getSelectedItem());
                ps.setString(7, (String) destBox.getSelectedItem());
                ps.setInt(8, Integer.parseInt(priceField.getText().trim()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Reservation successful!\nPNR: " + pnr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Reservation Failed: " + ex.getMessage());
            }
        });

        cancel.addActionListener(e -> {
            frame.dispose();
            showCancel();
        });

        frame.getContentPane().add(wrapCentered(panel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    static void showCancel() {
        JFrame frame = createFrame("Cancel Reservation");
        JPanel panel = createCenteredPanel();

        JTextField pnrField = createTextField();
        JButton cancelBtn = createButton("Confirm Cancel");

        panel.add(createLabel("Enter PNR to Cancel:"));
        panel.add(pnrField);
        panel.add(cancelBtn);

        cancelBtn.addActionListener(e -> {
            try {
                long pnr = Long.parseLong(pnrField.getText().trim());
                PreparedStatement ps = conn.prepareStatement("DELETE FROM reservations WHERE pnr=?");
                ps.setLong(1, pnr);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Reservation Cancelled.");
                } else {
                    JOptionPane.showMessageDialog(frame, "PNR not found.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Cancellation Failed: " + ex.getMessage());
            }
        });

        frame.getContentPane().add(wrapCentered(panel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // --- UI Helpers ---

    static JFrame createFrame(String title) {
        JFrame f = new JFrame(title);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(new Color(166, 182, 197)); // Steel Blue
        f.setLayout(new BorderLayout());
        return f;
    }

    static JPanel createCenteredPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setOpaque(false);
        return panel;
    }

    static JPanel wrapCentered(JPanel panel) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(panel);
        return wrapper;
    }

    static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setBackground(Color.WHITE);
        return field;
    }

    static JButton createButton(String label) {
        JButton btn = new JButton(label);
        btn.setPreferredSize(new Dimension(200, 30));
        btn.setBackground(new Color(82, 88, 106)); // Soft Gray Blue
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(50, 54, 65)); // Dark Grayish Blue
        return label;
    }
}
