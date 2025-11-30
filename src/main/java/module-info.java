module com.example.iitgamingclub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    // Export the main package
    exports com.example.iitgamingclub;

    // Open packages to JavaFX for FXML injection and Reflection
    opens com.example.iitgamingclub to javafx.fxml;
    opens com.example.iitgamingclub.controller to javafx.fxml;
    opens com.example.iitgamingclub.model to javafx.base; // Required for TableView
}