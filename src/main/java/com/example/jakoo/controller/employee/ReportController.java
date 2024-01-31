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
import com.example.jakoo.entity.WorkdaysNote;
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
import java.util.Optional;
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
    private static long currentSelectedEmployeeId = 0;
    private List<Workdays> workdays;

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

        datePicker.setOnAction(event -> {
            updateEmployeeReportInfo();
        });

        workdays = workdaysDAO.getAllWorkdays();
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
                updateEmployeeReportInfo();
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

    public void updateEmployeeReportInfo() {
        Timestamp date = Timestamp.valueOf(datePicker.getValue().atStartOfDay());
        if(currentSelectedEmployeeId > 0) {
            WorkdaysNote workdaysNote = workdaysNoteDAO.getWorkdayNoteByIdAndDate(currentSelectedEmployeeId, date);
            if(workdaysNote != null) {
                Optional<Workdays> workdaysOptional = workdays.stream().filter(e -> Objects.equals(e.getWorkdayId(), workdaysNote.getWorkdayId())).findFirst();
                workdaysOptional.ifPresent(value -> workdaysComboBox.setValue(value));
                workNoteTextArea.setText(workdaysNote.getNote());
                additionalTimeTextField.setText(workdaysNote.getPaid_additional_time().toString());
                additionalTimeNotPayedTextField.setText(workdaysNote.getUnpaid_additional_time().toString());
            } else {
                workdaysComboBox.setValue(workdays.stream().filter(e -> Objects.equals(e.getWorkdayId(), 1L)).findFirst().get());
                workNoteTextArea.clear();
                additionalTimeTextField.clear();
                additionalTimeNotPayedTextField.clear();
            }
            Note note = noteDAO.getNoteByIdAndDate(currentSelectedEmployeeId, date);
            if (note != null) {
                noteTextArea.setText(note.getText());
            }
            else { noteTextArea.clear();}
        }
    }

    public void saveReport() {
        WorkdaysNote workdaysNote = new WorkdaysNote();
        workdaysNote.setEmloyeeId(currentSelectedEmployeeId);
        workdaysNote.setWorkdayId(workdaysComboBox.getValue().getWorkdayId());
        workdaysNote.setNote(workNoteTextArea.getText());
        workdaysNote.setPaid_additional_time(Long.parseLong(additionalTimeTextField.getText()));
        workdaysNote.setUnpaid_additional_time(Long.parseLong(additionalTimeNotPayedTextField.getText()));
        workdaysNote.setDate(Timestamp.valueOf(datePicker.getValue().atStartOfDay()));
        workdaysNoteDAO.upsertWorkdaysNote(workdaysNote);
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
