package restaurant;

import java.awt.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.sql.*;

public class Frame1 extends JFrame {

    JLabel idLabel;
    JLabel passLabel;
    JLabel background;
    JLabel headerLabel;
    JLabel devInfo;

    JTextField id;
    JPasswordField pass;

    JButton submit;

    public Frame1() {
        setTitle("Restaurant Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //Background
        this.background = new JLabel(new ImageIcon("C:\\Users\\Asus\\Downloads\\Java-Project-Final (1)\\Java Project Final\\image\\burger.jpg"));

        this.init();

        add(background);
        background.setVisible(true);
        this.pack();
        this.setSize(700, 400);
        this.setLocationRelativeTo(null);
    }

    public void init() {
        headerLabel = new JLabel();
        this.headerLabel.setText("Restaurant Management System ");
        this.headerLabel.setBounds(160, 1, 400, 100);
        this.headerLabel.setFont(new Font("Geomanist", Font.BOLD, 25));
        headerLabel.setForeground(Color.darkGray);
        add(headerLabel);

        idLabel = new JLabel();
        this.idLabel.setText("Username");
        this.idLabel.setBounds(190, 110, 100, 50);
        this.idLabel.setFont(new Font(null, Font.BOLD, 20));
        idLabel.setForeground(Color.darkGray);
        add(idLabel);

        passLabel = new JLabel("Password");
        this.passLabel.setBounds(190, 165, 100, 50);
        this.passLabel.setFont(new Font(null, Font.BOLD, 20));
        passLabel.setForeground(Color.darkGray);
        add(passLabel);

        id = new JTextField();
        this.id.setBounds(300, 125, 200, 30);
        add(id);

        pass = new JPasswordField();
        this.add(pass);
        this.pass.setBounds(300, 175, 200, 30);

        this.id.setVisible(true);

        this.submit = new JButton("Login");
        this.submit.setBounds(400, 230, 100, 25);
        this.submit.setBackground(Color.darkGray);
        this.submit.setForeground(Color.white);
        add(submit);

        submit.addActionListener(this::submitActionPerformed);

    }

    public void submitActionPerformed(java.awt.event.ActionEvent evt) {

        if (this.id.getText().isEmpty() || this.pass.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.mkDataBase();

            String query = "SELECT * FROM admin WHERE login_id=? AND password=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, this.id.getText());
                preparedStatement.setString(2, this.pass.getText());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Successful login
                    this.hide();
                    Frame2new fn = new Frame2new();
                    fn.showButtonDemo();
                } else {
                    // Invalid credentials
                    JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}


