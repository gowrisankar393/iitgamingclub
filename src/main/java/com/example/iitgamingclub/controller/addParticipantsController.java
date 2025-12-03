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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;

public class addParticipantsController {
    @FXML private TextField txtName, txtEmail, txtSkill;
    @FXML private ComboBox<String> cmbGame, cmbRole;
    @FXML private Slider q1, q2, q3, q4, q5;
    @FXML private Label lblResult;
    @FXML private Button btnSave;
    private Participant tempP;
    private double xOffset = 0, yOffset = 0;

    @FXML public void initialize() {
        cmbGame.getItems().addAll("Valorant", "CS:GO", "FIFA", "DOTA 2", "League of Legends");
        cmbRole.getItems().addAll("Strategist", "Attacker", "Defender", "Supporter", "Coordinator");
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

    @FXML public void calculatePersonality() {
        try {
            int score = (int)((q1.getValue() + q2.getValue() + q3.getValue() + q4.getValue() + q5.getValue()) * 4);
            String type = score >= 90 ? "Leader" : score >= 70 ? "Balanced" : "Thinker";
            lblResult.setText("Result: " + type + " (" + score + ")");
            tempP = new Participant("TEMP", txtName.getText(), txtEmail.getText(), cmbGame.getValue(),
                    Integer.parseInt(txtSkill.getText()), cmbRole.getValue(), score, type);
            btnSave.setDisable(false);
        } catch (Exception e) { new Alert(Alert.AlertType.ERROR, "Check inputs").show(); }
    }

    @FXML public void saveParticipant() {
        DataManager dm = DataManager.getInstance();
        String file = dm.getCurrentActiveFile();
        if (file == null) {
            //logic to create new file if none exists (simplified)
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File f = fc.showSaveDialog(null);
            if (f != null) {
                try { dm.createNewFile(f.getAbsolutePath()); file = f.getAbsolutePath(); }
                catch (Exception e) { return; }
            } else return;
        }

        Participant p = new Participant(dm.generateNextId(file), tempP.getName(), tempP.getEmail(),
                tempP.getPreferredGame(), tempP.getSkillLevel(), tempP.getPreferredRole(),
                tempP.getPersonalityScore(), tempP.getPersonalityType());
        dm.addParticipant(file, p);

        try {
            dm.saveToFile(file);
            new Alert(Alert.AlertType.INFORMATION, "Saved!").showAndWait();
            cancel();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML public void cancel() {
        try {
            Stage cur = (Stage) btnSave.getScene().getWindow();
            cur.close();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/iitgamingclub/iitGamingClub.fxml"));
            Stage home = new Stage();
            home.initStyle(StageStyle.UNDECORATED);
            home.setScene(new Scene(root, 1280, 720));
            home.setMaximized(true);
            home.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}