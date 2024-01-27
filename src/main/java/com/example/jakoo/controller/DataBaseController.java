package com.example.jakoo.controller;

import javafx.scene.control.Alert;

import java.sql.*;

public class DataBaseController {

    private String dbName;
    private String url;
    private String user;
    private String password;

    public DataBaseController(String dbName, String url, String user, String password) {
        this.dbName = dbName;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void createDatabaseIfNotExists(Alert alert) {
        try {
            Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT datname FROM pg_database WHERE datname = '" + this.dbName + "'");
            if (!resultSet.next()) {
                statement.execute("CREATE DATABASE " + this.dbName);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFO");
                alert.setHeaderText("INFO");
                alert.setContentText("Created new database with name " + this.dbName);
                alert.showAndWait();
            }
            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("Cannot create database!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
