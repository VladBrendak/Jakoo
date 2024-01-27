package com.example.jakoo.controller;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.EmployeeGroupDAO;
import com.example.jakoo.entity.EmployeeGroup;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GroupsTabController implements Initializable {
    @FXML
    private ScrollPane groupsScrollPane;
    @FXML
    private FlowPane groupsFlowPane;
    @FXML
    private TextField groupNameTextField;
    private EmployeeGroupDAO groupDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DatabaseConnector connector = DatabaseConnector.getInstance();
        groupDAO = new EmployeeGroupDAO(connector);

        groupsFlowPane.setPadding(new Insets(5, 5, 5, 5));
        groupsFlowPane.setVgap(5);
        groupsFlowPane.setHgap(5);
        groupsFlowPane.setAlignment(Pos.TOP_CENTER);

        groupsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        groupsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        groupsScrollPane.setContent(groupsFlowPane);
        groupsScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                groupsFlowPane.setPrefWidth(bounds.getWidth());
                groupsFlowPane.setPrefHeight(bounds.getHeight());
            }
        });

        UpdateGroupsListView();
    }

    public void UpdateGroupsListView() {
        List<EmployeeGroup> employeeGroups = groupDAO.getAllEmployeeGroups();
        Pane[] panes = new Pane[employeeGroups.size()];
        groupsFlowPane.getChildren().clear();

        try {
            for (int i = 0; i < panes.length; i++) {
                final int j = i;

                panes[i] = FXMLLoader.load(getClass().getResource("/com/example/jakoo/group-item.fxml"));

                Label groupName = (Label) panes[i].getChildren().get(0);
                Button deleteButton = (Button) panes[i].getChildren().get(1);

                EmployeeGroup employeeGroup = employeeGroups.get(j);

                groupName.setText(employeeGroup.getName());

                panes[i].setOnMouseEntered(event -> {
                    panes[j].setStyle("-fx-background-color : #D3D3D3");
                });

                panes[i].setOnMouseExited(event -> {
                    panes[j].setStyle("-fx-background-color : #FFFFFF");
                });

                panes[i].setOnMouseClicked(event -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("groupName", employeeGroup.getName());
                    data.put("groupId", employeeGroup.getGroupId());
                    SceneManager.getInstance((Stage) groupNameTextField.getScene().getWindow()).switchScene("add-employee-to-group.fxml", data);
                });

                deleteButton.setOnMouseClicked(event -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Delete Dialog");
                    confirmation.setHeaderText("Delete Employee");
                    confirmation.setContentText("Are you sure you want to delete Emloyee named: \"" + employeeGroup.getName() + "\" ?");

                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            groupDAO.deleteEmployeeGroup(employeeGroup.getGroupId());
                            UpdateGroupsListView();
                        }
                    });
                });

                groupsFlowPane.getChildren().add(panes[i]);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void addGroup(ActionEvent actionEvent)
    {
        AtomicBoolean isInfoValid = new AtomicBoolean(true);
        String groupName = groupNameTextField.getText();

        if(groupName.isEmpty() || groupName.isBlank()) {
            isInfoValid.set(false);
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Attention!");
            confirmation.setHeaderText("Attention!");
            confirmation.setContentText("You cant add GROUP without name!");
            confirmation.showAndWait();
        }

        if(isInfoValid.get()) {
            EmployeeGroup employeeGroup = new EmployeeGroup(groupName);
            groupNameTextField.clear();
            groupDAO.addEmployeeGroup(employeeGroup, true);
            UpdateGroupsListView();
        }
    }
}
