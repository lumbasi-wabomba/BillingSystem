module com.system.billingsystem {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Java standard modules
    requires java.sql;
    requires java.desktop;

    // Open packages containing FXML controllers to javafx.fxml
    opens com.system.billingsystem.controller to javafx.fxml;

    // Open packages containing model classes to javafx.base for PropertyValueFactory reflection
    opens com.system.billingsystem.models to javafx.base, javafx.fxml;

    // Optionally open main package to FXML (if you use FXML there)
    opens com.system.billingsystem to javafx.fxml;

    // Export packages to allow external access
    exports com.system.billingsystem;
    exports com.system.billingsystem.models;
    exports com.system.billingsystem.controller;
}
