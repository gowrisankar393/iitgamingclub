package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.service.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class updateParticipantsController {
    @FXML private TextField txtSearchId, txtName, txtEmail;
    @FXML private ComboBox<String> cmbGame;
    @FXML private Button btnUpdate;
    private Participant currentP;
    private double xOffset, yOffset;

    @FXML public void initialize() {
        cmbGame.getItems().addAll("Valorant", "CS:GO", "FIFA", "DOTA 2", "League of Legends");
    }

    // Window Controls
    @FXML public void handleMousePressed(MouseEvent e) { xOffset = e.getSceneX(); yOffset = e.getSceneY(); }
    @FXML public void handleMouseDragged(MouseEvent e) {
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setX(e.getScreenX() - xOffset); s.setY(e.getScreenY() - yOffset);
    }
    @FXML public void handleMinimize(MouseEvent e) { ((Stage) ((Node) e.getSource()).getScene().getWindow()).setIconified(true); }
    @FXML public void handleMaximize(MouseEvent e) {
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setMaximized(!s.isMaximized());
    }

    @FXML public void searchParticipant() {
        DataManager dm = DataManager.getInstance();
        if (dm.getCurrentActiveFile() == null) return;
        currentP = dm.getParticipants(dm.getCurrentActiveFile()).stream()
                .filter(p -> p.getId().equalsIgnoreCase(txtSearchId.getText()))
                .findFirst().orElse(null);
        if (currentP != null) {
            txtName.setText(currentP.getName());
            txtEmail.setText(currentP.getEmail());
            cmbGame.setValue(currentP.getPreferredGame());
            txtName.setDisable(false); txtEmail.setDisable(false); cmbGame.setDisable(false); btnUpdate.setDisable(false);
        } else {
            new Alert(Alert.AlertType.WARNING, "Not Found").show();
        }
    }

    @FXML public void updateDetails() {
        if (currentP != null) {
            currentP.setName(txtName.getText());
            currentP.setEmail(txtEmail.getText());
            currentP.setPreferredGame(cmbGame.getValue());
            try {
                DataManager.getInstance().saveToFile(DataManager.getInstance().getCurrentActiveFile());
                new Alert(Alert.AlertType.INFORMATION, "Updated").showAndWait();
                cancel();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML public void cancel() {
        try {
            Stage cur = (Stage) txtName.getScene().getWindow();
            cur.close();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/iitgamingclub/iitGamingClub.fxml"));
            Stage home = new Stage();
            home.initStyle(StageStyle.UNDECORATED);
            home.setScene(new Scene(root, 1280, 720));
            home.setMaximized(true);
            home.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}