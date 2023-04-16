package ru.remsoftware.SQLConfiguration;

import java.sql.*;
import java.util.Properties;

public class DataBaseConnector {
    public Connection connectionDB() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Requests";

            Properties authorization = new Properties();
            authorization.put("user", "postgres");
            authorization.put("password", "rootroot");

            connection = DriverManager.getConnection(url, authorization);


        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("DATABASE CONNECTION ERROR!");
        }
        return connection;
    }


}
