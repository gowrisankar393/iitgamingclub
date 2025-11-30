package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.service.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class updateParticipantsController {
    @FXML private TextField txtSearchId, txtName, txtEmail;
    @FXML private ComboBox<String> cmbGame;
    @FXML private Button btnUpdate;

    private Participant currentP;

    @FXML public void initialize() {
        cmbGame.getItems().addAll("Valorant", "CS:GO", "FIFA", "DOTA 2", "League of Legends", "Chess", "Basketball");
    }

    @FXML public void searchParticipant() {
        DataManager dm = DataManager.getInstance();
        if (dm.getCurrentActiveFile() == null) {
            new Alert(Alert.AlertType.ERROR, "Please import a CSV file first.").show();
            return;
        }

        String id = txtSearchId.getText();
        currentP = dm.getParticipants(dm.getCurrentActiveFile()).stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);

        if (currentP != null) {
            txtName.setText(currentP.getName());
            txtEmail.setText(currentP.getEmail());
            cmbGame.setValue(currentP.getPreferredGame());

            txtName.setDisable(false);
            txtEmail.setDisable(false);
            cmbGame.setDisable(false);
            btnUpdate.setDisable(false);
        } else {
            new Alert(Alert.AlertType.WARNING, "ID not found").show();
        }
    }

    @FXML public void updateDetails() {
        if (currentP != null) {
            currentP.setName(txtName.getText());
            currentP.setEmail(txtEmail.getText());
            currentP.setPreferredGame(cmbGame.getValue());

            try {
                DataManager.getInstance().saveToFile(DataManager.getInstance().getCurrentActiveFile());
                new Alert(Alert.AlertType.INFORMATION, "Updated!").show();
                ((Stage) btnUpdate.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML public void cancel() { ((Stage) btnUpdate.getScene().getWindow()).close(); }
}