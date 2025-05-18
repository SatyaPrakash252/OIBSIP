package forms;

import javax.swing.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    JTextField userField;
    JPasswordField passField;

    public LoginForm() {
        setTitle("Login");
        setSize(300, 200);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(120, 30, 130, 25);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(120, 70, 130, 25);
        add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(90, 110, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals("admin") && password.equals("admin")) {
                dispose();
                new ReservationForm();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
