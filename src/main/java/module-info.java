module com.system.billingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.system.billingsystem;

    opens com.system.billingsystem.controller to javafx.fxml;
    opens com.system.billingsystem to javafx.fxml;

    exports com.system.billingsystem;
}
