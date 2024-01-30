package com.example.jakoo.controller.employee;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.EmployeeDAO;
import com.example.jakoo.entity.Employee;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmployeeTabController implements Initializable {
    @FXML
    private ScrollPane employeeScrollPane;
    @FXML
    private FlowPane employeeFlowPane;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private TextArea descriptionTextArea;
    private EmployeeDAO employeeDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DatabaseConnector connector = DatabaseConnector.getInstance();
        employeeDAO = new EmployeeDAO(connector);

        employeeFlowPane.setPadding(new Insets(5, 5, 5, 5));
        employeeFlowPane.setVgap(5);
        employeeFlowPane.setHgap(5);
        employeeFlowPane.setAlignment(Pos.TOP_CENTER);

        employeeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        employeeScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        employeeScrollPane.setContent(employeeFlowPane);
        employeeScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                employeeFlowPane.setPrefWidth(bounds.getWidth());
                employeeFlowPane.setPrefHeight(bounds.getHeight());
            }
        });

        UpdateEmployeeListView();
    }

    public void UpdateEmployeeListView() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        HBox[] hBoxes = new HBox[employees.size()];
        employeeFlowPane.getChildren().clear();

        try {
            for (int i = 0; i < hBoxes.length; i++) {
                final int j = i;

                hBoxes[i] = FXMLLoader.load(getClass().getResource("/com/example/jakoo/employee/employee-item.fxml"));

                VBox vBox = (VBox) hBoxes[i].getChildren().get(0);
                Button deleteButton = (Button) hBoxes[i].getChildren().get(1);
                Label nickname = (Label) vBox.getChildren().get(0);
                Label description = (Label) vBox.getChildren().get(1);

                Employee employee = employees.get(j);

                nickname.setText(employee.getName());
                description.setText(employee.getDescription());

                hBoxes[i].setOnMouseEntered(event -> {
                    hBoxes[j].setStyle("-fx-background-color : #D3D3D3");
                });

                hBoxes[i].setOnMouseExited(event -> {
                    hBoxes[j].setStyle("-fx-background-color : #FFFFFF");
                });

                deleteButton.setOnMouseClicked(event -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Delete Dialog");
                    confirmation.setHeaderText("Delete Employee");
                    confirmation.setContentText("Are you sure you want to delete Emloyee named: \"" + employee.getName() + "\" ?");

                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            employeeDAO.deleteEmployee(employee.getEmployeeId());
                            UpdateEmployeeListView();
                        }
                    });
                });

                employeeFlowPane.getChildren().add(hBoxes[i]);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void addEmployee(ActionEvent actionEvent)
    {
        AtomicBoolean isInfoValid = new AtomicBoolean(true);
        String nickname = nicknameTextField.getText();
        String description = descriptionTextArea.getText();

        if(nickname.isEmpty() || nickname.isBlank()) {
            isInfoValid.set(false);
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Attention!");
            confirmation.setHeaderText("Attention!");
            confirmation.setContentText("You cant add employee without name!");
            confirmation.showAndWait();
        }
        else if(description.isEmpty() || description.isBlank()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Attention!");
            confirmation.setHeaderText("Attention!");
            confirmation.setContentText("You gonna add employee without description, are you sure?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.NO) {
                    isInfoValid.set(false);
                }
            });
        }

        if(isInfoValid.get()) {
            Employee employee = new Employee(nicknameTextField.getText(), descriptionTextArea.getText());
            nicknameTextField.clear();
            descriptionTextArea.clear();
            employeeDAO.addEmployee(employee);
            UpdateEmployeeListView();
        }
    }

    public void makeReport() {
        SceneManager.getInstance((Stage) employeeScrollPane.getScene().getWindow()).switchScene("employee/report-employee.fxml", new HashMap<>());
    }
}
