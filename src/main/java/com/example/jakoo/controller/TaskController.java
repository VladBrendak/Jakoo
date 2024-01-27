package com.example.jakoo.controller;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.EmployeeDAO;
import com.example.jakoo.dao.TaskDAO;
import com.example.jakoo.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TaskController implements SceneManager.DataReceiver, Initializable {
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label tagsLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TextArea resultTextArea;
    @FXML
    private FlowPane employeeFlowPane;
    @FXML
    private FlowPane responsibleFlowPane;
    @FXML
    private Label creatationTimeLabel;
    private TaskTabItem taskTabItem;
    private Task task;
    private EmployeeDAO employeeDAO;
    private TaskDAO taskDAO;
    private TaskTabController taskTabController;
    private List<Employee> employeesList;
    private List<Employee> responsibleList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnector connector = DatabaseConnector.getInstance();
        employeeDAO = new EmployeeDAO(connector);
        taskDAO = new TaskDAO(connector);
    }
    @Override
    public void receiveData(Map<String, Object> data) {
        taskTabItem = (TaskTabItem) data.get("taskTabItem");
        taskTabController = (TaskTabController) data.get("taskTabControllerScene");
        String creationDateTime = (String) data.get("creationDateTime");
        task = taskTabItem.getTask();
        titleLabel.setText(task.getTitle());
        tagsLabel.setText("Tags: " + taskTabItem.getTags());
        priorityLabel.setText("Priority: " + taskTabItem.getPriorityValue());
        statusLabel.setText("Status: " + taskTabItem.getStatusValue());
        descriptionLabel.setText("Description: " + task.getDescription());
        creatationTimeLabel.setText("Created at: " + creationDateTime);
        resultTextArea.setText(task.getResults());

        setEmployeesTiles();
    }

    private void setEmployeesTiles()
    {
        employeeFlowPane.getChildren().clear();
        employeeFlowPane.setHgap(5);
        employeeFlowPane.setPadding(new Insets(0,5,0,5));
        employeeFlowPane.setAlignment(Pos.CENTER_LEFT);

        responsibleFlowPane.getChildren().clear();
        responsibleFlowPane.setHgap(5);
        responsibleFlowPane.setPadding(new Insets(0,5,0,5));
        responsibleFlowPane.setAlignment(Pos.CENTER_LEFT);

        employeesList = employeeDAO.getEmployeesInGroup(task.getEmployees());
        responsibleList = employeeDAO.getEmployeesInGroup(task.getResponsible());

        List<Label> employeeLabels = employeesList.stream().map(e -> {
            Label l = new Label();
            l.setPadding(new Insets(2,5,2,5));
            l.setStyle("-fx-background-color: #FFFFFF");
            l.setText(e.getName());
            return l;
        }).toList();

        List<Label> responsibleLabels = responsibleList.stream().map(e -> {
            Label l = new Label();
            l.setPadding(new Insets(2,5,2,5));
            l.setStyle("-fx-background-color: #FFFFFF");
            l.setText(e.getName());
            return l;
        }).toList();

        employeeFlowPane.getChildren().addAll(employeeLabels);
        responsibleFlowPane.getChildren().addAll(responsibleLabels);
    }


    public void goBack(ActionEvent actionEvent) {
        SceneManager.goBack();
    }

    public void saveTask(ActionEvent actionEvent) {
        task.setResults(resultTextArea.getText());
        taskDAO.updateTask(task);
        taskTabController.updateTaskView(null);
    }

    public void editTask(ActionEvent actionEvent) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskControllerScene", this);
        data.put("taskTabItem", taskTabItem);
        SceneManager.getInstance((Stage) titleLabel.getScene().getWindow()).switchScene("edit-task.fxml", data);
    }

    public void updateTaskInfo()
    {
        taskTabItem = taskDAO.getTaskTabItemById(task.getTaskId());
        task = taskTabItem.getTask();
        titleLabel.setText(task.getTitle());
        tagsLabel.setText("Tags: " + taskTabItem.getTags());
        priorityLabel.setText("Priority: " + taskTabItem.getPriorityValue());
        statusLabel.setText("Status: " + taskTabItem.getStatusValue());
        descriptionLabel.setText("Description: " + task.getDescription());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        creatationTimeLabel.setText(simpleDateFormat.format(task.getCreationDate()));
        resultTextArea.setText(task.getResults());

        setEmployeesTiles();
        taskTabController.updateTaskView(null);
    }
}
