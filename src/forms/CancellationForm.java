package forms;

import db.DBConnection;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class CancellationForm extends JFrame {
    public CancellationForm() {
        setTitle("Cancel Ticket");
        setSize(300, 200);
        setLayout(null);

        JLabel pnrLabel = new JLabel("Enter PNR No:");
        pnrLabel.setBounds(30, 30, 100, 25);
        add(pnrLabel);

        JTextField pnrField = new JTextField();
        pnrField.setBounds(130, 30, 100, 25);
        add(pnrField);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(90, 80, 100, 30);
        add(cancelBtn);

        cancelBtn.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                String sql = "DELETE FROM reservations WHERE pnr_no = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(pnrField.getText()));

                int status = ps.executeUpdate();
                if (status > 0) {
                    JOptionPane.showMessageDialog(this, "Ticket Cancelled");
                } else {
                    JOptionPane.showMessageDialog(this, "PNR not found");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
