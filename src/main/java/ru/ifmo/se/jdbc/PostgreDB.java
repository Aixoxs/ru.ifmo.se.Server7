package ru.ifmo.se.jdbc;

import ru.ifmo.se.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreDB {
    private Connection connection;

    {
        try {
            String user = "postgres";
            String password = "1234";
            String url = "jdbc:postgresql://localhost:5432/lab7";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
