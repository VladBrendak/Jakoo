package com.example.jakoo.controller;

import com.example.jakoo.DatabaseConnector;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private DatabaseConnector connector;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connector = DatabaseConnector.getInstance();
    }
}
