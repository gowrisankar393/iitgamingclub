package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.service.DataManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class showParticipantsController {
    @FXML private ComboBox<String> cmbFiles;
    @FXML private TableView<Participant> tblParticipants;
    @FXML private TableColumn<Participant, String> colId, colName, colGame, colRole, colType;
    @FXML private TableColumn<Participant, Integer> colSkill;

    @FXML public void initialize() {
        DataManager dm = DataManager.getInstance();
        if (dm.getLoadedFilePaths().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please import at least one participant file.").show();
            return;
        }

        cmbFiles.getItems().addAll(dm.getLoadedFilePaths());
        if (dm.getCurrentActiveFile() != null) {
            cmbFiles.getSelectionModel().select(dm.getCurrentActiveFile());
            loadTable();
        }

        // Data Binding via Reflection (PropertyValueFactory matches field names)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGame.setCellValueFactory(new PropertyValueFactory<>("preferredGame"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));
        colSkill.setCellValueFactory(new PropertyValueFactory<>("skillLevel"));
        colType.setCellValueFactory(new PropertyValueFactory<>("personalityType"));
    }

    @FXML public void loadTable() {
        String file = cmbFiles.getValue();
        if (file != null) {
            tblParticipants.setItems(FXCollections.observableArrayList(
                    DataManager.getInstance().getParticipants(file)
            ));
        }
    }
}