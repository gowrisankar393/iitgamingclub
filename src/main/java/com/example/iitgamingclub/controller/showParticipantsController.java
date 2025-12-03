package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.service.DataManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class showParticipantsController {
    @FXML private ComboBox<String> cmbFiles;
    @FXML private TableView<Participant> tblParticipants;
    @FXML private TableColumn<Participant, String> colId, colName, colGame, colRole, colType;
    @FXML private TableColumn<Participant, Integer> colSkill;
    private double xOffset, yOffset;

    @FXML public void initialize() {
        DataManager dm = DataManager.getInstance();
        cmbFiles.getItems().addAll(dm.getLoadedFilePaths());
        if (dm.getCurrentActiveFile() != null) {
            cmbFiles.getSelectionModel().select(dm.getCurrentActiveFile());
            loadTable();
        }
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGame.setCellValueFactory(new PropertyValueFactory<>("preferredGame"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("preferredRole"));
        colSkill.setCellValueFactory(new PropertyValueFactory<>("skillLevel"));
        colType.setCellValueFactory(new PropertyValueFactory<>("personalityType"));
    }

    //Window Controls
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

    @FXML public void loadTable() {
        if(cmbFiles.getValue() != null)
            tblParticipants.setItems(FXCollections.observableArrayList(DataManager.getInstance().getParticipants(cmbFiles.getValue())));
    }

    @FXML public void cancel() {
        try {
            Stage cur = (Stage) tblParticipants.getScene().getWindow();
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