package com.example.jakoo.controller;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.*;
import com.example.jakoo.entity.*;
import com.example.jakoo.entity.Priority;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class AddTaskController implements SceneManager.DataReceiver, Initializable {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField tagTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ScrollPane tagsScrollPane;
    @FXML
    private FlowPane tagsFlowPane;
    @FXML
    private ScrollPane employeesScrollPane;
    @FXML
    private FlowPane employeesFlowPane;
    @FXML
    private ScrollPane responsibleScrollPane;
    @FXML
    private FlowPane responsibleFlowPane;
    @FXML
    private ComboBox<Priority>  prioritiesComboBox;
    private EmployeeDAO employeeDAO;
    private PriorityDAO priorityDAO;
    private TaskDAO taskDAO;
    private TagDAO tagDAO;
    private TaskTagDAO taskTagDAO;
    private EmployeeGroupDAO employeeGroupDAO;
    private MemberDAO memberDAO;
    private TaskTabController taskTabController;
    private CheckBox[] checkBoxes;
    private CheckBox[] employeeCheckBoxes;
    private CheckBox[] responsibleCheckBoxes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DatabaseConnector connector = DatabaseConnector.getInstance();
        employeeDAO = new EmployeeDAO(connector);
        priorityDAO = new PriorityDAO(connector);
        taskDAO = new TaskDAO(connector);
        tagDAO = new TagDAO(connector);
        employeeGroupDAO = new EmployeeGroupDAO(connector);
        memberDAO = new MemberDAO(connector);
        taskTagDAO = new TaskTagDAO(connector);

        List<Priority> priorities = priorityDAO.getAllPriorities();

        prioritiesComboBox.setItems(FXCollections.observableArrayList(priorities));
        prioritiesComboBox.setPromptText("Choose Priority");

        tagsFlowPane.setPadding(new Insets(5, 5, 5, 5));
        tagsFlowPane.setVgap(5);
        tagsFlowPane.setHgap(5);
        tagsFlowPane.setAlignment(Pos.TOP_CENTER);

        tagsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        tagsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        tagsScrollPane.setContent(tagsFlowPane);
        tagsScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                tagsFlowPane.setPrefWidth(bounds.getWidth());
                tagsFlowPane.setPrefHeight(bounds.getHeight());
            }
        });

        employeesFlowPane.setPadding(new Insets(5, 5, 5, 5));
        employeesFlowPane.setVgap(5);
        employeesFlowPane.setHgap(5);
        employeesFlowPane.setAlignment(Pos.TOP_CENTER);

        employeesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        employeesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        employeesScrollPane.setContent(employeesFlowPane);
        employeesScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                employeesFlowPane.setPrefWidth(bounds.getWidth());
                employeesFlowPane.setPrefHeight(bounds.getHeight());
            }
        });

        responsibleFlowPane.setPadding(new Insets(5, 5, 5, 5));
        responsibleFlowPane.setVgap(5);
        responsibleFlowPane.setHgap(5);
        responsibleFlowPane.setAlignment(Pos.TOP_CENTER);

        responsibleScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        responsibleScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        responsibleScrollPane.setContent(responsibleFlowPane);
        responsibleScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                responsibleFlowPane.setPrefWidth(bounds.getWidth());
                responsibleFlowPane.setPrefHeight(bounds.getHeight());
            }
        });

        updateTagsView();
        updateMembersView();
    }

    public void addNewTag(ActionEvent action) {
        String newTagName = tagTextField.getText().trim();
        if (!newTagName.isEmpty() && !newTagName.isBlank()) {
            tagDAO.addTag(new Tag(newTagName));
            tagTextField.clear();
            updateTagsView();
        }
    }

    public void updateTagsView() {
        List<Tag> tags = tagDAO.getAllTags();
        checkBoxes = new CheckBox[tags.size()];
        tagsFlowPane.getChildren().clear();

        try {
            for (int i = 0; i < checkBoxes.length; i++) {
                final int j = i;

                CheckBox newCheckbox = new CheckBox();
                newCheckbox.setText(tags.get(j).getName());
                newCheckbox.setPadding(new Insets(5, 5, 5, 5));
                newCheckbox.setUserData(tags.get(j));
                newCheckbox.setMaxWidth(282);
                newCheckbox.setStyle("-fx-background-color: #ffffff");

                if(newCheckbox.getText().length() > 13) {
                    newCheckbox.setPrefSize(282, 30);
                } else {
                    newCheckbox.setPrefSize(138, 30);
                }

                checkBoxes[i] = newCheckbox;

                checkBoxes[i].setOnMouseEntered(event -> {
                    checkBoxes[j].setStyle("-fx-background-color : #D3D3D3");
                });

                checkBoxes[i].setOnMouseExited(event -> {
                    checkBoxes[j].setStyle("-fx-background-color : #FFFFFF");
                });

                tagsFlowPane.getChildren().add(checkBoxes[i]);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void updateMembersView() {
        List<Employee> members = employeeDAO.getAllEmployees();
        employeeCheckBoxes = new CheckBox[members.size()];
        responsibleCheckBoxes = new CheckBox[members.size()];
        employeesFlowPane.getChildren().clear();
        responsibleFlowPane.getChildren().clear();

        try {
            for (int i = 0; i < employeeCheckBoxes.length; i++) {
                final int j = i;

                employeeCheckBoxes[i] = createEmployeeCheckBox(members.get(j));
                responsibleCheckBoxes[i] = createEmployeeCheckBox(members.get(j));

                employeesFlowPane.getChildren().add(employeeCheckBoxes[i]);
                responsibleFlowPane.getChildren().add(responsibleCheckBoxes[i]);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public CheckBox createEmployeeCheckBox(Employee employee) {
        CheckBox newCheckbox = new CheckBox();
        newCheckbox.setText(employee.getName());
        newCheckbox.setPadding(new Insets(5, 5, 5, 5));
        newCheckbox.setUserData(employee);
        newCheckbox.setPrefSize(235, 30);
        newCheckbox.setMaxWidth(235);
        newCheckbox.setStyle("-fx-background-color: #ffffff");

        newCheckbox.setOnMouseEntered(event -> {
            newCheckbox.setStyle("-fx-background-color : #D3D3D3");
        });

        newCheckbox.setOnMouseExited(event -> {
            newCheckbox.setStyle("-fx-background-color : #FFFFFF");
        });

        return newCheckbox;
    }

    @Override
    public void receiveData(Map<String, Object> data) {
        taskTabController = (TaskTabController) data.get("taskTabControllerScene");
    }

    public void goBack(ActionEvent actionEvent) {
        SceneManager.goBack();
    }

    public void addTask(ActionEvent actionEvent) {

        String taskTitle = titleTextField.getText();

        List<Long> selectedEmployeeIds = Arrays.stream(employeeCheckBoxes)
                .filter(CheckBox::isSelected)
                .map(checkBox -> ((Employee) checkBox.getUserData()).getEmployeeId())
                .toList();

        List<Long> selectedResponsibleIds = Arrays.stream(responsibleCheckBoxes)
                .filter(CheckBox::isSelected)
                .map(checkBox -> ((Employee) checkBox.getUserData()).getEmployeeId())
                .toList();

        List<Long> selectedTagsIds = Arrays.stream(checkBoxes)
                .filter(CheckBox::isSelected)
                .map(checkBox -> ((Tag) checkBox.getUserData()).getTagID())
                .toList();

        if(!taskTitle.isBlank() && !taskTitle.isEmpty()
                && prioritiesComboBox.getValue() != null
                && !descriptionTextArea.getText().isEmpty() && !descriptionTextArea.getText().isBlank()
                && selectedEmployeeIds.size() > 0
                && selectedResponsibleIds.size() > 0
                && selectedTagsIds.size() > 0) {

            String employeeTitle = taskTitle + "_eg";
            String responsibleTitle = taskTitle + "_rg";

            EmployeeGroup newEmployeeGroup = new EmployeeGroup(employeeTitle);
            EmployeeGroup newResponsibleGroup = new EmployeeGroup(responsibleTitle);

            employeeGroupDAO.addEmployeeGroup(newEmployeeGroup, false);
            employeeGroupDAO.addEmployeeGroup(newResponsibleGroup, false);

            Long employeeGroupId = employeeGroupDAO.getGroupIdByName(employeeTitle);
            Long responsibleGroupId = employeeGroupDAO.getGroupIdByName(responsibleTitle);

            memberDAO.addMembersByIds(employeeGroupId, selectedEmployeeIds);
            memberDAO.addMembersByIds(responsibleGroupId, selectedResponsibleIds);

            Task task = new Task();

            task.setEmployees(employeeGroupId);
            task.setResponsible(responsibleGroupId);
            task.setPriority(prioritiesComboBox.getValue().getPriorityId());

            task.setTitle(taskTitle);
            task.setDescription(descriptionTextArea.getText());
            task.setStatus(1L);
            task.setCreationDate(new Timestamp(System.currentTimeMillis()));
            taskDAO.addTask(task);

            Long taskId = taskDAO.getTaskIdByName(taskTitle);
            taskTagDAO.addTaskTagsByIds(taskId, selectedTagsIds);

            taskTabController.updateTaskView(null);
            goBack(actionEvent);

        } else {
            Alert confirmation = new Alert(Alert.AlertType.WARNING);
            confirmation.setTitle("Info Dialog");
            confirmation.setHeaderText("Some data missed!");
            confirmation.setContentText("Please check data validity!!!\nChoose all data that required! You may miss one.");
            confirmation.showAndWait();
        }
    }
}
