package com.example.jakoo.controller.task;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.PriorityDAO;
import com.example.jakoo.dao.TaskDAO;
import com.example.jakoo.entity.Task;
import com.example.jakoo.entity.TaskTabItem;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TaskTabController implements Initializable {
    private static final long PAGE_SIZE = 20;
    private long currentPageNumber = 1;
    private static long MAX_PAGE_NUMBER;
    private boolean isSearch = false;
    @FXML
    private ScrollPane taskScrollPane;
    @FXML
    private FlowPane taskFlowPane;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField searchTagsTextField;
    private TaskDAO taskDAO;
    private PriorityDAO priorityDAO;
    private SimpleDateFormat simpleDateFormat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DatabaseConnector connector = DatabaseConnector.getInstance();
        taskDAO = new TaskDAO(connector);
        priorityDAO = new PriorityDAO(connector);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");

        MAX_PAGE_NUMBER = taskDAO.getTaskPageNumber(PAGE_SIZE);

        taskFlowPane.setPadding(new Insets(5, 5, 5, 5));
        taskFlowPane.setVgap(5);
        taskFlowPane.setHgap(5);
        taskFlowPane.setAlignment(Pos.TOP_CENTER);

        taskScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        taskScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        taskScrollPane.setContent(taskFlowPane);
        taskScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                taskFlowPane.setPrefWidth(bounds.getWidth());
                taskFlowPane.setPrefHeight(bounds.getHeight());
            }
        });

        updateTaskView(null);
    }

    public void addTask(ActionEvent actionEvent) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskTabControllerScene", this);
        SceneManager.getInstance((Stage) taskScrollPane.getScene().getWindow()).switchScene("task/add-task.fxml", data);
    }

    public void updateTaskView(List<TaskTabItem> newTaskList)
    {
        List<TaskTabItem> tasks;
        if(newTaskList != null && newTaskList.size() > 0)
        {
            tasks = newTaskList;
        } else {
            if(isSearch) {
                String searchedTags = formatTagString(searchTagsTextField.getText());
                tasks = taskDAO.searchByTitleAndTag(PAGE_SIZE, currentPageNumber, searchTextField.getText(), searchedTags);
            } else
            {
                tasks = taskDAO.getTaskTabItemsByPage(PAGE_SIZE, currentPageNumber);
            }
        }

        AnchorPane[] panes = new AnchorPane[tasks.size()];
        taskFlowPane.getChildren().clear();

        try {
            for (int i = 0; i < panes.length; i++) {
                final int j = i;

                panes[i] = FXMLLoader.load(getClass().getResource("/com/example/jakoo/task/task-item.fxml"));

                Label title = (Label) panes[i].getChildren().get(0);
                Label tags = (Label) panes[i].getChildren().get(1);
                Label priority = (Label) panes[i].getChildren().get(2);
                Label status = (Label) panes[i].getChildren().get(3);
                Label creationDate = (Label) panes[i].getChildren().get(4);
                Button deleteButton = (Button) panes[i].getChildren().get(5);

                TaskTabItem taskTabItem = tasks.get(j);
                Task task = taskTabItem.getTask();

                title.setText(task.getTitle());
                tags.setText(taskTabItem.getTags());
                priority.setText("Priority: " + taskTabItem.getPriorityValue());
                status.setText("Status: " + taskTabItem.getStatusValue());

                String creationDateTime = simpleDateFormat.format(task.getCreationDate());
                creationDate.setText(creationDateTime);

                panes[i].setOnMouseEntered(event -> {
                    panes[j].setStyle("-fx-background-color : #D3D3D3");
                });

                panes[i].setOnMouseExited(event -> {
                    panes[j].setStyle("-fx-background-color : #FFFFFF");
                });

                panes[i].setOnMouseClicked(event -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("taskTabItem", taskTabItem);
                    data.put("taskTabControllerScene", this);
                    data.put("creationDateTime", creationDateTime);
                    SceneManager.getInstance((Stage) taskScrollPane.getScene().getWindow()).switchScene("task/task.fxml", data);
                });

                deleteButton.setOnMouseClicked(event -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Delete Dialog");
                    confirmation.setHeaderText("Delete Task");
                    confirmation.setContentText("Are you sure you want to delete Task named: \"" + task.getTitle() + "\" ?");

                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            taskDAO.deleteTask(task);
                            updateTaskView(null);
                        }
                    });
                });

                taskFlowPane.getChildren().add(panes[i]);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void nextTaskPage(){
        if (currentPageNumber < MAX_PAGE_NUMBER){
            currentPageNumber++;
            updateTaskView(null);
        }
    }

    public void previousTaskPage(){
        if(currentPageNumber > 1) {
            currentPageNumber--;
            updateTaskView(null);
        }
    }

    public static String formatTagString(String input) {
        String[] tags = input.toLowerCase().split(",");
        StringBuilder formattedString = new StringBuilder();

        for (String tag : tags) {
            String trimmedTag = tag.trim();
            if (!trimmedTag.isEmpty()) {
                if (formattedString.length() > 0) {
                    formattedString.append(", ");
                }
                formattedString.append("'").append(trimmedTag).append("'");
            }
        }

        return formattedString.toString();
    }

    public void search() {
        String searchedTags = formatTagString(searchTagsTextField.getText());
        isSearch = true;
        currentPageNumber = 1;
        updateTaskView(taskDAO.searchByTitleAndTag(PAGE_SIZE, currentPageNumber, searchTextField.getText(), searchedTags));
    }

    public void resetSearch(){
        isSearch = false;
        updateTaskView(null);
        searchTextField.clear();
        searchTagsTextField.clear();
    }
}
