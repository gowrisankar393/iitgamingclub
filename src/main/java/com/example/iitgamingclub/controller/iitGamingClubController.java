package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.service.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;
import java.io.IOException;

public class iitGamingClubController {

    // --- WINDOW DRAGGING LOGIC ---
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    public void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    // --- WINDOW BUTTON LOGIC ---
    @FXML
    public void handleMinimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void handleMaximize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Toggle Maximize
        stage.setMaximized(!stage.isMaximized());
    }

    // Note: Reusing your existing handleExit for the close button
    @FXML public void handleExit() { System.exit(0); }

    // --- EXISTING NAVIGATION LOGIC (Unchanged) ---

    private void switchScene(String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/iitgamingclub/" + fxmlFile));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(fxmlFile.replace(".fxml", ""));
        // Keep new windows undecorated too if you want consistent style
        // stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    @FXML
    public void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Participant CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                DataManager.getInstance().importFile(file);
                showAlert(Alert.AlertType.INFORMATION, "Success", "File imported successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        }
    }

    @FXML public void goToAdd() throws IOException { switchScene("addParticipants.fxml"); }
    @FXML public void goToRemove() throws IOException { switchScene("removeParticipants.fxml"); }
    @FXML public void goToUpdate() throws IOException { switchScene("updateParticipants.fxml"); }
    @FXML public void goToShow() throws IOException { switchScene("showParticipants.fxml"); }
    @FXML public void goToGenerate() throws IOException { switchScene("createTeam.fxml"); }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}