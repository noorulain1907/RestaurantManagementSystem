package restaurant;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.*;

public class UpdateFood {

    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JLabel id, name, price, quantity;
    private static int count = 0;
    GridLayout experimentLayout = new GridLayout(0, 2);
    ResultSet rs;

    UpdateFood() {

        prepareGUI();
    }

    public static void main(String[] args) {
        UpdateFood swingControlDemo = new UpdateFood();
        swingControlDemo.showButtonDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Update!");
        mainFrame.setSize(700, 400);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.getContentPane().setBackground(Color.lightGray);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                mainFrame.setVisible(false);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);

        statusLabel.setSize(350, 400);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    public void showButtonDemo() {

        headerLabel.setText("Restaurant Management System");
        headerLabel.setFont(new Font(null, Font.BOLD, 27));

        name = new JLabel("Enter Name");
        JTextField tf2 = new JTextField();
        tf2.setSize(100, 30);

        price = new JLabel("Enter Price");
        JTextField tf3 = new JTextField();
        tf3.setSize(100, 30);

        quantity = new JLabel("Enter Quantity");
        JTextField tf4 = new JTextField();
        tf4.setSize(100, 30);

        JButton okButton = new JButton("UPDATE");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String foodName = tf2.getText();
                String priceStr = tf3.getText();
                String quantityStr = tf4.getText();

                if (foodName.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PreparedStatement pst;
                DBConnection con = new DBConnection();
                try {
                    if (!tf2.getText().matches("^[a-zA-Z\\s]+$")) {
                        JOptionPane.showMessageDialog(null, "Invalid product name!");
                        return;
                    }

                    // Check if the price is a valid number
                    if (!tf3.getText().matches("^\\d+(\\.\\d+)?$")) {
                        JOptionPane.showMessageDialog(null, "Invalid price!");
                        return;
                    }

                    // Check if the quantity is a valid integer
                    if (!tf4.getText().matches("^\\d+$")) {
                        JOptionPane.showMessageDialog(null, "Invalid quantity!");
                        return;
                    }
                    if (!isFoodExists(foodName)) {
                        JOptionPane.showMessageDialog(null, "Food not found in the database!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    pst = con.mkDataBase().prepareStatement("UPDATE restaurant.food SET quantity= ?, price=? where food_name = ?");
                    pst.setString(3, tf2.getText());
                    pst.setDouble(2, Double.parseDouble(tf3.getText()));
                    pst.setInt(1, Integer.parseInt(tf4.getText()));
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Done Updating " + tf2.getText());
                    mainFrame.setVisible(false);

                } catch (Exception ex) {
                    System.out.println(ex);
                    System.out.println("Error");
                    JOptionPane.showMessageDialog(null, "Inserting Error : " + tf2.getText());
                } finally {

                }
            }
        });

        JPanel jp = new JPanel();
        jp.add(name);
        jp.add(tf2);
        jp.add(price);
        jp.add(tf3);
        jp.add(quantity);
        jp.add(tf4);

        jp.setSize(200, 200);
        jp.setLayout(experimentLayout);
        controlPanel.add(jp);
        jp.add(okButton);

        mainFrame.setVisible(true);

        mainFrame.setLocationRelativeTo(null);
    }

    private boolean isFoodExists(String foodName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DBConnection dbConnection = new DBConnection();
            connection = dbConnection.mkDataBase();

            String query = "SELECT * FROM restaurant.food WHERE food_name = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, foodName);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Returns true if the food exists
        } finally {
            // Close resources (ResultSet, PreparedStatement, and Connection)
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
