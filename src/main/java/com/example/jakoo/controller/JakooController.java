package com.example.jakoo.controller;

import com.example.jakoo.DatabaseConfigJson;
import com.example.jakoo.Jakoo;
import com.example.jakoo.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.exception.FlywaySqlException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class JakooController {
    @FXML
    private TextField dataBaseName;
    @FXML
    private TextField adminName;
    @FXML
    private TextField adminPassword;
    @FXML
    private CheckBox dontAskAgainCheckBox;
    @FXML
    private Button connectButton;
    private Alert alert;
    private String url = "jdbc:postgresql://localhost:5432/";
    private DataBaseController dataBaseController;
    DatabaseConfigJson databaseConfigJson;

    public void startApp(ActionEvent actionEvent) throws IOException {
        connectButton.setText("Loading...");
        boolean isOk = true;
        String dbName = dataBaseName.getText().toLowerCase();
        String user = adminName.getText();
        String password = adminPassword.getText();

        if(password.isBlank() || password.isEmpty())
        {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("IMPORTANT");
            alert.setHeaderText("You have some issue");
            alert.setContentText("You forgot to type password");
            alert.showAndWait();
            isOk = false;
        }
        if(dbName.isBlank() || dbName.isEmpty())
        {
            dbName = dataBaseName.getPromptText();
        }
        if(user.isBlank() || user.isEmpty())
        {
            user = adminName.getPromptText();
        }

        if(isOk) {
            dataBaseController = new DataBaseController(dbName, url, user, password);
            dataBaseController.createDatabaseIfNotExists(alert);

            try {
                Flyway flyway = Flyway.configure()
                        .dataSource(url + dbName, user, password)
                        .locations("filesystem:src/main/resources/db/migration")
                        .schemas("public")
                        .baselineOnMigrate(true)
                        .load();

                flyway.migrate();

            } catch (FlywaySqlException e) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR");
                alert.setHeaderText("ERROR");
                alert.setContentText("Your Admin name or password is incorrect!!!");
                alert.showAndWait();
                e.printStackTrace();
                return;
            }

            if(dontAskAgainCheckBox.isSelected())
            {
                databaseConfigJson = new DatabaseConfigJson();
                try {
                    databaseConfigJson.setSkipLogin(true);
                    databaseConfigJson.setUrl(url+dbName);
                    databaseConfigJson.setUser(user);
                    databaseConfigJson.setPassword(password);

                } catch (FileNotFoundException e)
                {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("ERROR");
                    alert.setContentText("Cannot rewrite config file!!!");
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }
//            switchToMain();
            Node anyNode = adminName;
            Stage currentStage = (Stage) anyNode.getScene().getWindow();
            SceneManager.getInstance(currentStage).switchScene("main-view.fxml", new HashMap<>());
        }
        else {
            connectButton.setText("Connect");
        }
    }

    public void switchToMain() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Jakoo.class.getResource("main-view.fxml"));
        Parent root = loader.load();

        Scene secondScene = new Scene(root, 600, 400);

        Node anyNode = adminName;
        Stage currentStage = (Stage) anyNode.getScene().getWindow();

        // Add your stylesheets if needed
        // secondScene.getStylesheets().add(Jakoo.class.getResource("/styles/style.css").toExternalForm());

        currentStage.setScene(secondScene);
        currentStage.show();
    }
}