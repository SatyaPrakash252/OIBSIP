package forms;

import db.DBConnection;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ReservationForm extends JFrame {
    JTextField nameField, ageField, genderField, trainField, classField, dateField, sourceField, destField;

    public ReservationForm() {
        setTitle("Reservation");
        setSize(400, 400);
        setLayout(null);

        JLabel[] labels = {
                new JLabel("Name:"), new JLabel("Age:"), new JLabel("Gender:"),
                new JLabel("Train No:"), new JLabel("Class:"), new JLabel("Journey Date (YYYY-MM-DD):"),
                new JLabel("Source:"), new JLabel("Destination:")
        };

        JTextField[] fields = {
                nameField = new JTextField(), ageField = new JTextField(), genderField = new JTextField(),
                trainField = new JTextField(), classField = new JTextField(), dateField = new JTextField(),
                sourceField = new JTextField(), destField = new JTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(30, 30 + i * 40, 150, 25);
            fields[i].setBounds(190, 30 + i * 40, 150, 25);
            add(labels[i]);
            add(fields[i]);
        }

        JButton reserveBtn = new JButton("Reserve");
        reserveBtn.setBounds(70, 360, 100, 30);
        add(reserveBtn);

        JButton cancelBtn = new JButton("Go to Cancellation");
        cancelBtn.setBounds(190, 360, 160, 30);
        add(cancelBtn);

        reserveBtn.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                String sql = "INSERT INTO reservations (name, age, gender, train_no, class_type, journey_date, source, destination) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setInt(2, Integer.parseInt(ageField.getText()));
                ps.setString(3, genderField.getText());
                ps.setString(4, trainField.getText());
                ps.setString(5, classField.getText());
                ps.setString(6, dateField.getText());
                ps.setString(7, sourceField.getText());
                ps.setString(8, destField.getText());

                int status = ps.executeUpdate();
                if (status > 0) {
                    JOptionPane.showMessageDialog(this, "Reservation successful!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        cancelBtn.addActionListener(e -> {
            dispose();
            new CancellationForm();
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
