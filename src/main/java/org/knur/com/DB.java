package org.knur.com;

import java.sql.*;
import java.util.Properties;

public class DB {
    static DB instance = new DB();

    private DB() {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static DB getInstance()
    {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/knur";
        Properties props = new Properties();
        props.setProperty("user", "knurzysko");
        props.setProperty("password", "jp100procent");
        props.setProperty("ssl", "false");

        return DriverManager.getConnection(url, props);
    }
}
