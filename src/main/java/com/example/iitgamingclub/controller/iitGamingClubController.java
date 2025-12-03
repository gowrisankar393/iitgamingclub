package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.service.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;

public class iitGamingClubController {
    @FXML private StackPane rootPane;
    private double xOffset = 0, yOffset = 0;

    //window controls
    @FXML public void handleMousePressed(MouseEvent event) { xOffset = event.getSceneX(); yOffset = event.getSceneY(); }
    @FXML public void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
    @FXML public void handleMinimize(MouseEvent e) { ((Stage) ((Node) e.getSource()).getScene().getWindow()).setIconified(true); }
    @FXML public void handleMaximize(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }
    @FXML public void handleExit() { System.exit(0); }

    private void switchScene(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/iitgamingclub/" + fxml));
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.UNDECORATED); //to make sure theres no windows default title bar
        newStage.setTitle("IIT Gaming Club");
        newStage.setScene(new Scene(root, 1280, 720));
        newStage.setMaximized(true);
        newStage.show();

        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML public void handleImport() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File f = fc.showOpenDialog(null);
        if(f != null) {
            try {
                DataManager.getInstance().importFile(f);
                new Alert(Alert.AlertType.INFORMATION, "Imported: " + f.getName()).show();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML public void goToAdd() throws IOException { switchScene("addParticipants.fxml"); }
    @FXML public void goToRemove() throws IOException { switchScene("removeParticipants.fxml"); }
    @FXML public void goToUpdate() throws IOException { switchScene("updateParticipants.fxml"); }
    @FXML public void goToShow() throws IOException { switchScene("showParticipants.fxml"); }
    @FXML public void goToGenerate() throws IOException { switchScene("createTeam.fxml"); }
}