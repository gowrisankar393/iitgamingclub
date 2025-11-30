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
import java.util.List;

public class removeParticipantsController {
    @FXML private TextField txtSearchId;
    @FXML private Label lblInfo;
    @FXML private Button btnCancel; // For scene access
    private double xOffset, yOffset;

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

    @FXML public void searchAndDelete() {
        DataManager dm = DataManager.getInstance();
        String file = dm.getCurrentActiveFile();
        if (file == null) {
            lblInfo.setText("Please import a file first.");
            return;
        }

        List<Participant> list = dm.getParticipants(file);
        Participant p = list.stream().filter(x -> x.getId().equalsIgnoreCase(txtSearchId.getText())).findFirst().orElse(null);

        if (p != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + p.getName() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                list.remove(p);
                try {
                    dm.saveToFile(file);
                    lblInfo.setText("Deleted Successfully.");
                } catch(Exception e) { lblInfo.setText("Error Saving."); }
            }
        } else {
            lblInfo.setText("ID Not Found.");
        }
    }

    @FXML public void cancel() {
        try {
            // Logic to return Home
            Stage cur = (Stage) txtSearchId.getScene().getWindow();
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