module com.example.jakoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.json;
    requires org.flywaydb.core;

    opens com.example.jakoo to javafx.fxml, java.compiler;
    exports com.example.jakoo;
    exports com.example.jakoo.controller;
    opens com.example.jakoo.controller to javafx.fxml;
    exports com.example.jakoo.controller.task;
    opens com.example.jakoo.controller.task to javafx.fxml;
    exports com.example.jakoo.controller.employee;
    opens com.example.jakoo.controller.employee to javafx.fxml;
    exports com.example.jakoo.controller.group;
    opens com.example.jakoo.controller.group to javafx.fxml;

}


