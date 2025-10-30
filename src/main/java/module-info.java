module com.system.billingsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.system.billingsystem to javafx.fxml;
    exports com.system.billingsystem;
}