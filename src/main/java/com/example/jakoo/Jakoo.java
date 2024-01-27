package com.example.jakoo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Jakoo extends Application {
    FXMLLoader fxmlLoader;
    Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        DatabaseConfigJson databaseConfigJson = new DatabaseConfigJson();
        SceneManager sceneManager = SceneManager.getInstance(stage);

        if(databaseConfigJson.getSkipLogin())
        {
//            fxmlLoader = new FXMLLoader(Jakoo.class.getResource("main-view.fxml"));
//            scene = new Scene(fxmlLoader.load(), 600, 400);
//            stage.setTitle("Jakoo");
//            stage.setScene(scene);
//            stage.show();
            sceneManager.switchScene("main-view.fxml", new HashMap<>());
        } else {
//            fxmlLoader = new FXMLLoader(Jakoo.class.getResource("startup-view.fxml"));
//            scene = new Scene(fxmlLoader.load(), 600, 400);
//            stage.setTitle("Jakoo");
//            stage.setScene(scene);
//            stage.show();
            sceneManager.switchScene("startup-view.fxml", new HashMap<>());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}