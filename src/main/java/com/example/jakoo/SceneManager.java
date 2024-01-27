package com.example.jakoo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

public class SceneManager {
    private static SceneManager instance; // Singleton instance
    private static final Stack<SceneInfo> sceneStack = new Stack<>();
    private static Stage primaryStage;

    // Private constructor to prevent instantiation
    private SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Public method to get the singleton instance
    public static SceneManager getInstance(Stage primaryStage) {
        if (instance == null) {
            instance = new SceneManager(primaryStage);
        }
        return instance;
    }

    public void switchScene(String fxmlPath, Map<String, Object> data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene newScene = new Scene(root);

            // Pass data to the controller
            Object controller = loader.getController();
            if (controller instanceof DataReceiver) {
                ((DataReceiver) controller).receiveData(data);
            }

            SceneInfo sceneInfo = new SceneInfo(newScene, fxmlPath, data);
            sceneStack.push(sceneInfo);

            primaryStage.setScene(newScene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public static void goBack() {
        if (sceneStack.size() > 1) {
            sceneStack.pop(); // Remove the current scene
            SceneInfo previousSceneInfo = sceneStack.peek();
            primaryStage.setScene(previousSceneInfo.getScene());
            primaryStage.show();
        }
    }

    private static class SceneInfo {
        private final Scene scene;
        private final String fxmlPath;
        private final Map<String, Object> data;

        public SceneInfo(Scene scene, String fxmlPath, Map<String, Object> data) {
            this.scene = scene;
            this.fxmlPath = fxmlPath;
            this.data = data;
        }

        public Scene getScene() {
            return scene;
        }

        public String getFxmlPath() {
            return fxmlPath;
        }

        public Map<String, Object> getData() {
            return data;
        }
    }

    public interface DataReceiver {
        void receiveData(Map<String, Object> data);
    }
}
