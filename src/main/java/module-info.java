module com.example.iitgamingclub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    exports com.example.iitgamingclub;

    opens com.example.iitgamingclub to javafx.fxml;
    opens com.example.iitgamingclub.controller to javafx.fxml;
    opens com.example.iitgamingclub.model to javafx.base;
}