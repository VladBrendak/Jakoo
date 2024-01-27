package com.example.jakoo.controller;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.EmployeeDAO;
import com.example.jakoo.dao.MemberDAO;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AddEmployeeToGroupController implements SceneManager.DataReceiver, Initializable {
    @FXML
    private ScrollPane addEmployeeToGroupScrollPane;
    @FXML
    private FlowPane addEmployeeToGroupFlowPane;
    @FXML
    private ScrollPane GroupMembersScrollPane;
    @FXML
    private FlowPane GroupMembersFlowPane;
    @FXML
    private Label groupNameLabel;
    private Long groupId;
    private MemberDAO memberDAO;
    private EmployeeDAO employeeDAO;
    private List<Employee> groupMembersList;
    private List<Long> previousMembersIds;
    private List<Employee> employeesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnector connector = DatabaseConnector.getInstance();
        memberDAO = new MemberDAO(connector);
        employeeDAO = new EmployeeDAO(connector);

        addEmployeeToGroupFlowPane.setPadding(new Insets(5, 5, 5, 5));
        addEmployeeToGroupFlowPane.setVgap(5);
        addEmployeeToGroupFlowPane.setHgap(5);
        addEmployeeToGroupFlowPane.setAlignment(Pos.TOP_CENTER);

        addEmployeeToGroupScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        addEmployeeToGroupScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        addEmployeeToGroupScrollPane.setContent(addEmployeeToGroupFlowPane);
        addEmployeeToGroupScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                addEmployeeToGroupFlowPane.setPrefWidth(bounds.getWidth());
                addEmployeeToGroupFlowPane.setPrefHeight(bounds.getHeight());
            }
        });

        GroupMembersFlowPane.setPadding(new Insets(5, 5, 5, 5));
        GroupMembersFlowPane.setVgap(5);
        GroupMembersFlowPane.setHgap(5);
        GroupMembersFlowPane.setAlignment(Pos.TOP_CENTER);

        GroupMembersScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        GroupMembersScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        GroupMembersScrollPane.setContent(GroupMembersFlowPane);
        GroupMembersScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                GroupMembersFlowPane.setPrefWidth(bounds.getWidth());
                GroupMembersFlowPane.setPrefHeight(bounds.getHeight());
            }
        });
    }

    @Override
    public void receiveData(Map<String, Object> data) {
        groupId = Long.valueOf(data.get("groupId").toString());
        groupNameLabel.setText(data.get("groupName").toString());
        setData();
    }

    public void setData(){
        groupMembersList = employeeDAO.getEmployeesInGroup(groupId);
        employeesList = employeeDAO.getEmployeesNotInGroup(groupId);
        previousMembersIds = groupMembersList.stream().map(Employee::getEmployeeId).collect(Collectors.toList());
        updateEmloyeesListView();
        updateMembersListView();
    }

    public void updateMembersListView()
    {
        GroupMembersFlowPane.getChildren().clear();
        Pane[] panes = new Pane[groupMembersList.size()];
        try {
            for (int i = 0; i < panes.length; i++) {
                final int j = i;
                panes[i] = FXMLLoader.load(getClass().getResource("/com/example/jakoo/add-employee-to-group-tile.fxml"));
                Label userName = (Label) panes[i].getChildren().get(0);

                Employee member = groupMembersList.get(j);
                userName.setText(member.getName());

                panes[i].setOnMouseEntered(event -> {
                    panes[j].setStyle("-fx-background-color : #D3D3D3");
                });

                panes[i].setOnMouseExited(event -> {
                    panes[j].setStyle("-fx-background-color : #FFFFFF");
                });

                panes[i].setOnMouseClicked(event -> {
                    employeesList.add(member);
                    groupMembersList.remove(member);
                    updateMembersListView();
                    updateEmloyeesListView();
                });

                GroupMembersFlowPane.getChildren().add(panes[i]);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateEmloyeesListView()
    {
        addEmployeeToGroupFlowPane.getChildren().clear();
        Pane[] panes = new Pane[employeesList.size()];
        try {
            for (int i = 0; i < panes.length; i++) {
                final int j = i;
                panes[i] = FXMLLoader.load(getClass().getResource("/com/example/jakoo/add-employee-to-group-tile.fxml"));
                Label userName = (Label) panes[i].getChildren().get(0);

                Employee employee = employeesList.get(j);
                userName.setText(employee.getName());

                panes[i].setOnMouseEntered(event -> {
                    panes[j].setStyle("-fx-background-color : #D3D3D3");
                });

                panes[i].setOnMouseExited(event -> {
                    panes[j].setStyle("-fx-background-color : #FFFFFF");
                });

                panes[i].setOnMouseClicked(event -> {
                    groupMembersList.add(employee);
                    employeesList.remove(employee);
                    updateMembersListView();
                    updateEmloyeesListView();
                });

                addEmployeeToGroupFlowPane.getChildren().add(panes[i]);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveGroupMembers(ActionEvent actionEvent) {
        List<Long> newIds = groupMembersList.stream()
                .map(Employee::getEmployeeId)
                .filter(el -> !previousMembersIds.contains(el)).toList();

        String deleteOldIds = employeesList.stream()
                .map(Employee::getEmployeeId)
                .filter(el -> previousMembersIds.contains(el))
                .toList()
                .toString();

        deleteOldIds = deleteOldIds.substring(1, deleteOldIds.length()-1);

        memberDAO.deleteMembersByIds(deleteOldIds);
        memberDAO.addMembersByIds(groupId, newIds);

        setData();
    }

    public void goBack(ActionEvent actionEvent)
    {
        SceneManager.goBack();
    }
}
