package db;

import users.User;

import java.sql.*;

public class DBConnector {
    private String dbURL;
    private Connection connection;

    public DBConnector(final String dbUrL) {
        this.dbURL = dbUrL;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.dbURL,"root","root");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getDbUrl() {
        return dbURL;
    }

    public Connection getConnection() {
        return connection;
    }
}
