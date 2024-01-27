package com.example.jakoo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static DatabaseConnector instance;
    private DatabaseConfigJson config;

    public DatabaseConnector() {}

    public DatabaseConnector(DatabaseConfigJson config) {
        this.config = config;
    }

    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector(new DatabaseConfigJson());
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        String url = config.getUrl();
        String user = config.getUser();
        String password = config.getPassword();

        return DriverManager.getConnection(url, user, password);
    }
}