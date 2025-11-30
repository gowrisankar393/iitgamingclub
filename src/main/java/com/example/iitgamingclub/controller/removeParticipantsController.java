package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.service.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class removeParticipantsController {
    @FXML private TextField txtSearchId;
    @FXML private Label lblInfo;

    @FXML
    public void searchAndDelete() {
        DataManager dm = DataManager.getInstance();
        String file = dm.getCurrentActiveFile();

        if (file == null) {
            new Alert(Alert.AlertType.ERROR, "Please import a CSV file first.").show();
            return;
        }

        String id = txtSearchId.getText();
        List<Participant> list = dm.getParticipants(file);
        Participant target = list.stream().filter(p -> p.getId().equalsIgnoreCase(id)).findFirst().orElse(null);

        if (target != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + target.getName() + "?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();
            if (confirm.getResult() == ButtonType.YES) {
                list.remove(target);
                try {
                    dm.saveToFile(file);
                    lblInfo.setText("Deleted successfully.");
                } catch (Exception e) {
                    lblInfo.setText("Error saving file.");
                }
            }
        } else {
            lblInfo.setText("No user found with ID: " + id);
        }
    }
}