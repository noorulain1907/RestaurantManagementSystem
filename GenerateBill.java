package restaurant;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GenerateBill extends JFrame {

    JTextField food, quantity;
    String[] columnNames = {"Food Name",
        "Quantity",
        "Price"
    };
    JTable cart;

    // JLabel totalP = new JLabel("TOTAL PRICE : 0.00");
    JLabel totalP = new JLabel();
    Object data[][] = new Object[100][3];
    int i = 0;
    double totalprice = 0;
    ArrayList<foodCart> foodList = new ArrayList<>();

    GenerateBill() {
        JPanel jp1 = new JPanel();

        setBackground(Color.red);
        //jp1.getContentPane().setBackground(Color.orange);

        this.setLayout(new GridLayout(2, 2));

        JLabel a = new JLabel("Food Name : ");
        jp1.add(a);
        food = new JTextField(50);
        jp1.add(food);
        JLabel b = new JLabel("Quantity : ");
        jp1.add(b);
        quantity = new JTextField(50);
        jp1.add(quantity);

        JButton ok = new JButton("OK");

        JPanel jp2 = new JPanel();
        jp2.setSize(700, 400);
        jp1.add(ok);
        
// Inside your ActionListener for the "OK" button
ok.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        PreparedStatement pst;
        DBConnection con = new DBConnection();
        ResultSet rs;

        try {
            pst = con.mkDataBase().prepareStatement("select price from restaurant.food where food_name = ?");
            pst.setString(1, food.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                foodCart f = new foodCart();
                f.name = food.getText();
                f.quantity = Integer.parseInt(quantity.getText());
                f.totalPer = f.quantity * rs.getDouble("price");
                totalprice += f.quantity * rs.getDouble("price");

                foodList.add(f);

                // Get the existing model from the JTable
//                DefaultTableModel model = (DefaultTableModel) cart.getModel();
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                // Add the new row to the model
                model.addRow(new Object[]{f.name, f.quantity, f.totalPer});

                // Update total price label
                totalP.setText("TOTAL Price : " + Double.toString(totalprice) + "tk");

                // Clear input fields
                food.setText("");
                quantity.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Food not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
});
          

        
/*
        ok.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        PreparedStatement pst;
        DBConnection con = new DBConnection();
        ResultSet rs;

        try {
            pst = con.mkDataBase().prepareStatement("select price from restaurant.food where food_name = ?");
            pst.setString(1, food.getText());
            rs = pst.executeQuery();

            while (rs.next()) {
                foodCart f = new foodCart();
                f.name = food.getText();
                f.quantity = Integer.parseInt(quantity.getText());
                f.totalPer = f.quantity * rs.getDouble("price");
                totalprice += f.quantity * rs.getDouble("price");

                foodList.add(f);

                // Create a new DefaultTableModel with columnNames
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                // Populate the model with existing data
                for (Object[] rowData : data) {
                    model.addRow(rowData);
                }

                // Add the new row to the model
                model.addRow(new Object[]{f.name, f.quantity, f.totalPer});

                // Set the new model to the JTable
                cart.setModel(model);

                // Update total price label
                totalP.setText("TOTAL Price : " + Double.toString(totalprice) + " rupees");

                // Clear input fields
                food.setText("");
                quantity.setText("");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
});

        
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PreparedStatement pst;
                DBConnection con = new DBConnection();
                ResultSet rs;
                try {
                    pst = con.mkDataBase().prepareStatement("select price from restaurant.food where food_name = ?");
                    pst.setString(1, food.getText());
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        foodCart f = new foodCart();
                        f.name = food.getText();
                        f.quantity = Integer.parseInt(quantity.getText());
                        f.totalPer = f.quantity * rs.getDouble("price");
                        totalprice += f.quantity * rs.getDouble("price");

                        foodList.add(f);
                        data[i][0] = f.name;
                        data[i][1] = Integer.parseInt(quantity.getText());
                        data[i][2] = f.quantity * rs.getDouble("price");
                        i++;
                        food.setText("");
                        quantity.setText("");
                        DefaultTableModel model = (DefaultTableModel) cart.getModel();
                        model.setRowCount(0);
                        cart = new JTable(data, columnNames);
                        System.out.println(totalprice);
                        removeAll();
                        //Total price not refreshing
                        //   totalP.setText("TOTAL Price : " + Double.toString(totalprice) + "");
                        totalP.revalidate();
                        totalP.repaint();
                        revalidate();
                        repaint();
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
*/
        cart = new JTable(data, columnNames);
        cart.setSize(300, 450);
        //cart.setEnabled(false);
//       jp2.setLayout(new GridLayout(1,1));
        jp2.setLayout(new FlowLayout());
        jp2.add(totalP);
        jp2.add(new JScrollPane(cart, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        JButton checkOut = new JButton("CheckOut");
        checkOut.setSize(40, 50);
        jp2.add(checkOut);
        checkOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 1;
                for (foodCart fc : foodList) {
                    System.out.println(count + ": Food Name : " + fc.name + " Quantity : " + fc.quantity + " Price : " + fc.totalPer + " rupees");

                }
                double vat = 15;
                System.out.println("Total Cost : " + (totalprice + totalprice * vat) + " rupees");

                JOptionPane.showMessageDialog(null, "Total Cost : " + (totalprice + totalprice * vat / 100) + " rupees with gst " + vat + "%");
                hide();
            }
        });

        this.add(jp1);
        this.add(jp2);
        this.setSize(600, 550);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    class foodCart {

        String name;
        Double totalPer;
        int quantity;
    }

}
