package com.example.jakoo.controller.employee;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.SceneManager;
import com.example.jakoo.dao.EmployeeDAO;
import com.example.jakoo.dao.NoteDAO;
import com.example.jakoo.dao.WorkdaysDAO;
import com.example.jakoo.dao.WorkdaysNoteDAO;
import com.example.jakoo.entity.Employee;
import com.example.jakoo.entity.Note;
import com.example.jakoo.entity.Workdays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    @FXML
    private ScrollPane employeeScrollPane;
    @FXML
    private FlowPane employeeFlowPane;
    @FXML
    private TextField additionalTimeTextField;
    @FXML
    private TextField additionalTimeNotPayedTextField;
    @FXML
    private TextArea workNoteTextArea;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Workdays> workdaysComboBox;
    private EmployeeDAO employeeDAO;
    private NoteDAO noteDAO;
    private WorkdaysNoteDAO workdaysNoteDAO;
    private Font font;
    private Insets insets;
    private static int lastSelectedEmployeeId = 0;
    private static long currentSelectedEmployeeId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        font = new Font(14);
        insets = new Insets(15,15,15,15);

        DatabaseConnector connector = DatabaseConnector.getInstance();
        WorkdaysDAO workdaysDAO = new WorkdaysDAO(connector);
        workdaysNoteDAO = new WorkdaysNoteDAO(connector);
        noteDAO = new NoteDAO(connector);
        employeeDAO = new EmployeeDAO(connector);

        additionalTimeTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!Character.isDigit(event.getCharacter().charAt(0))) {
                event.consume();
            }
        });

        additionalTimeNotPayedTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!Character.isDigit(event.getCharacter().charAt(0)) && !(event.getCharacter().equals("-") && additionalTimeTextField.getCaretPosition() == 0)) {
                event.consume();
            }
        });

        datePicker.setValue(LocalDate.now());

        List<Workdays> workdays = workdaysDAO.getAllWorkdays();
        workdaysComboBox.setItems(FXCollections.observableArrayList(workdays));
        workdaysComboBox.setValue(workdays.stream().filter(e -> Objects.equals(e.getWorkdayId(), 1L)).findFirst().get());

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

        updateEmployeeListView();
    }

    public void updateEmployeeListView() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        Label[] labels = new Label[employees.size()];
        employeeFlowPane.getChildren().clear();

        for (int i = 0; i < labels.length; i++) {
            final int j = i;

            Employee employee = employees.get(j);
            labels[i] = createEmployeeItem(employee);

            employeeFlowPane.getChildren().add(labels[i]);

            labels[i].setOnMouseEntered(event -> {
                labels[j].setStyle("-fx-background-color : #D3D3D3");
            });

            labels[i].setOnMouseExited(event -> {
                if (j == lastSelectedEmployeeId) {
                    labels[j].setStyle("-fx-background-color : #D3D3D3");
                } else {
                    labels[j].setStyle("-fx-background-color : #FFFFFF");
                }
            });

            labels[i].setOnMouseClicked(event -> {
                labels[lastSelectedEmployeeId].setStyle("-fx-background-color : #FFFFFF");
                lastSelectedEmployeeId = j;
                currentSelectedEmployeeId = employee.getEmployeeId();
                labels[j].setStyle("-fx-background-color : #D3D3D3");
                updateEmployeReportInfo(employee);
            });
        }
    }

    public Label createEmployeeItem(Employee e) {
        Label l = new Label();
        l.setPadding(insets);
        l.setPrefWidth(employeeFlowPane.getPrefWidth() - 22);
        l.setStyle("-fx-background-color: white;");
        l.setWrapText(true);
        l.setFont(font);
        l.setText(e.getName());
        return l;
    }

    public void updateEmployeReportInfo(Employee e) {

    }

    public void saveReport() {

    }

    public void saveNote() {
        Note n = new Note();
        n.setEmloyeeId(currentSelectedEmployeeId);
        n.setText(noteTextArea.getText());
        n.setDate(Timestamp.valueOf(datePicker.getValue().atStartOfDay()));
        noteDAO.upsertNote(n);
    }

    public void goBack() {
        SceneManager.goBack();
    }
}
