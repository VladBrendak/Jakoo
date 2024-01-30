package com.example.jakoo.controller.task;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.*;
import com.example.jakoo.entity.*;
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
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class TaskEditController implements SceneManager.DataReceiver, Initializable {
    @FXML
    private TextField taskTitle;
    @FXML
    private TextField addTag;
    @FXML
    private ScrollPane tagsScrollPane;
    @FXML
    private FlowPane tagsFlowPane;
    @FXML
    private ComboBox<Priority> priority;
    @FXML
    private ComboBox<Status> status;
    @FXML
    private DatePicker creationDate;
    @FXML
    private TextField creationTime;
    @FXML
    private TextArea description;
    @FXML
    private ScrollPane employeeScrollPane;
    @FXML
    private FlowPane employeeFlowPane;
    @FXML
    private ScrollPane responsibleScrollPane;
    @FXML
    private FlowPane responsibleFlowPane;
    @FXML
    private TextArea result;
    private TaskTabItem taskTabItem;
    private Task task;
    private PriorityDAO priorityDAO;
    private StatusDAO statusDAO;
    private TagDAO tagDAO;
    private EmployeeDAO employeeDAO;
    private MemberDAO memberDAO;
    private TaskTagDAO taskTagDAO;
    private TaskDAO taskDAO;
    private CheckBox[] tagCheckBoxes;
    private CheckBox[] employeeCheckBoxes;
    private CheckBox[] responsibleCheckBoxes;
    private List<Long> employeeBefore;
    private List<Long> responsibleBefore;
    private List<Long> tagBefore;
    private TaskController taskController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnector connector = DatabaseConnector.getInstance();

        priorityDAO = new PriorityDAO(connector);
        statusDAO = new StatusDAO(connector);
        tagDAO = new TagDAO(connector);
        employeeDAO = new EmployeeDAO(connector);
        memberDAO = new MemberDAO(connector);
        taskTagDAO = new TaskTagDAO(connector);
        taskDAO = new TaskDAO(connector);

        tagBefore = new ArrayList<>();
        employeeBefore = new ArrayList<>();
        responsibleBefore = new ArrayList<>();

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
    }

    public void addNewTag(ActionEvent action) {
        String newTagName = addTag.getText().trim();
        if (!newTagName.isEmpty() && !newTagName.isBlank()) {
            tagDAO.addTag(new Tag(newTagName));
            addTag.clear();
            updateTagsView();
        }
    }

    public void updateTagsView() {
        List<Tag> tags = tagDAO.getAllTagsWithRelative(task.getTaskId());
        tagCheckBoxes = new CheckBox[tags.size()];
        tagsFlowPane.getChildren().clear();

        try {
            for (int i = 0; i < tagCheckBoxes.length; i++) {
                final int j = i;

                Tag currentTag = tags.get(j);

                if(currentTag.getRelateTo()){
                    tagBefore.add(currentTag.getTagID());
                }

                CheckBox newCheckbox = new CheckBox();
                newCheckbox.setFocusTraversable(false);
                newCheckbox.setText(currentTag.getName());
                newCheckbox.setPadding(new Insets(5, 5, 5, 5));
                newCheckbox.setUserData(currentTag);
                newCheckbox.setMaxWidth(345);
                newCheckbox.setStyle("-fx-background-color: #ffffff");
                newCheckbox.setSelected(currentTag.getRelateTo());

                if(newCheckbox.getText().length() > 13) {
                    newCheckbox.setPrefSize(345, 30);
                } else {
                    newCheckbox.setPrefSize(170, 30);
                }

                tagCheckBoxes[i] = newCheckbox;

                tagCheckBoxes[i].setOnMouseEntered(event -> {
                    tagCheckBoxes[j].setStyle("-fx-background-color : #D3D3D3");
                });

                tagCheckBoxes[i].setOnMouseExited(event -> {
                    tagCheckBoxes[j].setStyle("-fx-background-color : #FFFFFF");
                });

                tagsFlowPane.getChildren().add(tagCheckBoxes[i]);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void updateMembersView() {
        List<Employee> employeeList = employeeDAO.getAllEmployeesWithChecked(task.getEmployees());
        List<Employee> responsibleList = employeeDAO.getAllEmployeesWithChecked(task.getResponsible());
        employeeCheckBoxes = new CheckBox[employeeList.size()];
        responsibleCheckBoxes = new CheckBox[responsibleList.size()];
        employeeFlowPane.getChildren().clear();
        responsibleFlowPane.getChildren().clear();

        try {
            for (int i = 0; i < employeeCheckBoxes.length; i++) {
                final int j = i;

                Employee currentEmployee = employeeList.get(j);
                Employee currentResponsible = responsibleList.get(j);

                if(currentEmployee.getChecked()) {
                    employeeBefore.add(currentEmployee.getEmployeeId());
                }

                if(currentResponsible.getChecked()) {
                    responsibleBefore.add(currentResponsible.getEmployeeId());
                }

                employeeCheckBoxes[i] = createEmployeeCheckBox(currentEmployee, currentEmployee.getChecked());
                responsibleCheckBoxes[i] = createEmployeeCheckBox(currentResponsible, currentResponsible.getChecked());

                employeeFlowPane.getChildren().add(employeeCheckBoxes[i]);
                responsibleFlowPane.getChildren().add(responsibleCheckBoxes[i]);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public CheckBox createEmployeeCheckBox(Employee employee, boolean isChecked) {
        CheckBox newCheckbox = new CheckBox();
        newCheckbox.setFocusTraversable(false);
        newCheckbox.setText(employee.getName());
        newCheckbox.setPadding(new Insets(5, 5, 5, 5));
        newCheckbox.setUserData(employee);
        newCheckbox.setPrefSize(235, 30);
        newCheckbox.setMaxWidth(235);
        newCheckbox.setStyle("-fx-background-color: #ffffff");
        newCheckbox.setSelected(isChecked);

        return newCheckbox;
    }

    @Override
    public void receiveData(Map<String, Object> data) {
        taskTabItem = (TaskTabItem) data.get("taskTabItem");
        taskController = (TaskController) data.get("taskControllerScene");
        task = taskTabItem.getTask();
        taskTitle.setText(task.getTitle());

        description.setText(task.getDescription());

        LocalDateTime dateTime = task.getCreationDate().toLocalDateTime();
        creationDate.setValue(dateTime.toLocalDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        creationTime.setText(dateTime.toLocalTime().format(formatter));

        result.setText(task.getResults());

        List<Priority> priorities = priorityDAO.getAllPriorities();
        priority.setItems(FXCollections.observableArrayList(priorities));
        priority.setValue(priorities.stream().filter(e -> Objects.equals(e.getPriorityId(), task.getPriority())).findFirst().get());

        List<Status> statuses = statusDAO.getAllStatuses();
        status.setItems(FXCollections.observableArrayList(statuses));
        status.setValue(statuses.stream().filter(e -> Objects.equals(e.getStatusId(), task.getStatus())).findFirst().get());

        updateTagsView();
        updateMembersView();
    }

    private static Timestamp convertStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveChanges(ActionEvent actionEvent)
    {
        Pattern pattern = Pattern.compile("(2[0-3]|[01]?[0-9]):([0-5]?[0-9])");
        if (pattern.matcher(creationTime.getText()).matches()) {
            Task newTask = new Task();
            long taskID = task.getTaskId();
            newTask.setTaskId(taskID);
            newTask.setTitle(taskTitle.getText());
            newTask.setDescription(description.getText());
            newTask.setResults(result.getText());
            newTask.setPriority(priority.getValue().getPriorityId());
            newTask.setStatus(status.getValue().getStatusId());
            newTask.setEmployees(task.getEmployees());
            newTask.setResponsible(task.getResponsible());
            newTask.setCreationDate(convertStringToTimestamp(creationDate.getValue() + " " + creationTime.getText()));

            taskDAO.updateTask(newTask);

            // Tags
            List<Long> selectedTagsIds = Arrays.stream(tagCheckBoxes)
                    .filter(CheckBox::isSelected)
                    .map(checkBox -> ((Tag) checkBox.getUserData()).getTagID())
                    .toList();

            List<Long> newTags =
                    selectedTagsIds.stream()
                            .filter(el -> !tagBefore.contains(el)).toList();

            List<Long> deleteTags = tagBefore.stream()
                    .filter(el -> !selectedTagsIds.contains(el)).toList();

            // Employees
            List<Long> selectedEmployeeIds = Arrays.stream(employeeCheckBoxes)
                    .filter(CheckBox::isSelected)
                    .map(checkBox -> ((Employee) checkBox.getUserData()).getEmployeeId())
                    .toList();

            List<Long> newEmployees =
                    selectedEmployeeIds.stream()
                            .filter(el -> !employeeBefore.contains(el)).toList();

            String deleteEmployees = employeeBefore.stream()
                    .filter(el -> !selectedEmployeeIds.contains(el)).toList().toString();

            deleteEmployees = deleteEmployees.substring(1, deleteEmployees.length()-1);

            // Responsible
            List<Long> selectedResponsibleIds = Arrays.stream(responsibleCheckBoxes)
                    .filter(CheckBox::isSelected)
                    .map(checkBox -> ((Employee) checkBox.getUserData()).getEmployeeId())
                    .toList();

            List<Long> newResponsible =
                    selectedResponsibleIds.stream()
                            .filter(el -> !responsibleBefore.contains(el)).toList();

            String deleteResponsible = responsibleBefore.stream()
                    .filter(el -> !selectedResponsibleIds.contains(el)).toList().toString();

            deleteResponsible = deleteResponsible.substring(1, deleteResponsible.length()-1);

            taskTagDAO.addTaskTagsByIds(taskID, newTags);
            taskTagDAO.deleteTaskTagsByIds(taskID, deleteTags);

            memberDAO.addMembersByIds(task.getEmployees(), newEmployees);
            memberDAO.deleteMembersFromGroupByIds(deleteEmployees, task.getEmployees());

            memberDAO.addMembersByIds(task.getResponsible(), newResponsible);
            memberDAO.deleteMembersFromGroupByIds(deleteResponsible, task.getResponsible());

            taskController.updateTaskInfo();
            SceneManager.goBack();
        } else {
            Alert confirmation = new Alert(Alert.AlertType.WARNING);
            confirmation.setTitle("Info Dialog");
            confirmation.setHeaderText("Some data missed!");
            confirmation.setContentText("Please check data validity!!!\nSet appropriate time!!!");
            creationTime.requestFocus();
            confirmation.showAndWait();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        SceneManager.goBack();
    }
}
