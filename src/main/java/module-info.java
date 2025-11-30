module com.example.iitgamingclub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    // Export main package
    exports com.example.iitgamingclub;

    // Allow JavaFX to access internal classes for FXML loading and Reflection
    opens com.example.iitgamingclub to javafx.fxml;
    opens com.example.iitgamingclub.controller to javafx.fxml;
    opens com.example.iitgamingclub.model to javafx.base; // Required for TableView data binding
}