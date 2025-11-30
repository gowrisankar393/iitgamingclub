package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.service.DataManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class addParticipantsController {
    @FXML private TextField txtName, txtEmail, txtSkill;
    @FXML private ComboBox<String> cmbGame, cmbRole;
    @FXML private Slider q1, q2, q3, q4, q5;
    @FXML private Label lblResult;
    @FXML private Button btnSave;

    private Participant tempParticipant;

    // --- WINDOW DRAGGING VARIABLES ---
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        cmbGame.getItems().addAll("Valorant", "CS:GO", "FIFA", "DOTA 2", "League of Legends", "Chess", "Basketball");
        cmbRole.getItems().addAll("Strategist", "Attacker", "Defender", "Supporter", "Coordinator");
    }

    // --- WINDOW CONTROL METHODS (Copied from Home Controller) ---
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

    @FXML
    public void handleMinimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void handleMaximize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    // --- LOGIC METHODS ---

    @FXML
    public void calculatePersonality() {
        try {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String game = cmbGame.getValue();
            String role = cmbRole.getValue();
            String skillText = txtSkill.getText();

            if (skillText.isEmpty() || name.isEmpty() || game == null || role == null) {
                new Alert(Alert.AlertType.WARNING, "Please fill all fields correctly.").show();
                return;
            }

            int skill = Integer.parseInt(skillText);
            if (skill < 1 || skill > 10) {
                new Alert(Alert.AlertType.WARNING, "Skill must be 1-10.").show();
                return;
            }

            // Calculation Logic: Sum * 4
            int total = (int)(q1.getValue() + q2.getValue() + q3.getValue() + q4.getValue() + q5.getValue());
            int score = total * 4;

            String type;
            if (score >= 90) type = "Leader";
            else if (score >= 70) type = "Balanced";
            else type = "Thinker";

            lblResult.setText("Score: " + score + " (" + type + ")");

            // Create temp object (ID is generated later)
            tempParticipant = new Participant("TEMP", name, email, game, skill, role, score, type);
            btnSave.setDisable(false);

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Skill level must be a number.").show();
        }
    }

    @FXML
    public void saveParticipant() {
        DataManager dm = DataManager.getInstance();
        String activeFile = dm.getCurrentActiveFile();

        // If no file is loaded, prompt to Import or Create New
        if (activeFile == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No CSV loaded. Create a new file?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                File f = fc.showSaveDialog(null);
                if (f != null) {
                    try {
                        dm.createNewFile(f.getAbsolutePath());
                        activeFile = f.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                } else return;
            } else return;
        }

        // Generate ID and Save
        String newId = dm.generateNextId(activeFile);
        Participant finalP = new Participant(newId, tempParticipant.getName(), tempParticipant.getEmail(),
                tempParticipant.getPreferredGame(), tempParticipant.getSkillLevel(),
                tempParticipant.getPreferredRole(), tempParticipant.getPersonalityScore(),
                tempParticipant.getPersonalityType());

        dm.addParticipant(activeFile, finalP);
        try {
            dm.saveToFile(activeFile);
            new Alert(Alert.AlertType.INFORMATION, "Saved to " + activeFile).show();
            cancel();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Save failed: " + e.getMessage()).show();
        }
    }

    @FXML public void cancel() {
        ((Stage) btnSave.getScene().getWindow()).close(); // Or navigate back to home if preferred
    }
}