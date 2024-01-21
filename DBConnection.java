package restaurant;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    public Connection con;
    String url = "jdbc:mysql://localhost:3307/restaurant";
    String db = "restaurant";
    String user = "root";
    String pass = "ahsan";

    public Connection mkDataBase() throws SQLException {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);

        }
        return con;
    }

}
