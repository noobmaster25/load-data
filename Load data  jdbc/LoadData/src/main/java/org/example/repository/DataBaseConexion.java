package org.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConexion {
    private static final String url = "jdbc:mysql://localhost:3306/test_data?useSSL=false";
    private static final String username = "luigi";
    private static final String password = "contrasenia";


    public static Connection getConnection(){
        Connection con = null;

        try {

            con = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
    public static void closeConnection(Connection con){
        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
