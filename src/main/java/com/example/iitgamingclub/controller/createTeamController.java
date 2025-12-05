package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.model.Team;
import com.example.iitgamingclub.service.DataManager;
import com.example.iitgamingclub.service.TeamMatchingService;
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

public class createTeamController {
    @FXML private TextField txtTeamSize;
    @FXML private ComboBox<String> cmbFiles;
    @FXML private TextArea txtOutput;
    private double xOffset, yOffset;

    @FXML public void initialize() {
        cmbFiles.getItems().addAll(DataManager.getInstance().getLoadedFilePaths());
    }

    //window controls
    @FXML public void handleMousePressed(MouseEvent e) {
        xOffset = e.getSceneX(); yOffset = e.getSceneY();
    }
    @FXML public void handleMouseDragged(MouseEvent e) {
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setX(e.getScreenX() - xOffset); s.setY(e.getScreenY() - yOffset);
    }
    @FXML public void handleMinimize(MouseEvent e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).setIconified(true);
    }
    @FXML public void handleMaximize(MouseEvent e) {
        Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
        s.setMaximized(!s.isMaximized());
    }

    @FXML public void generate() {
        if(cmbFiles.getValue() == null) { txtOutput.setText("Select File"); return; }
        try {
            int n = Integer.parseInt(txtTeamSize.getText());
            List<Participant> pool = DataManager.getInstance().getParticipants(cmbFiles.getValue());
            TeamMatchingService svc = new TeamMatchingService();
            List<Team> teams = svc.generateTeams(pool, n);
            StringBuilder sb = new StringBuilder();
            for(Team t : teams) sb.append(t.toString()).append("\n");
            txtOutput.setText(sb.toString());
        }
        catch(Exception e) {
            txtOutput.setText("Invalid Input");
        }
    }

    @FXML public void cancel() {
        try {
            Stage cur = (Stage) txtOutput.getScene().getWindow();
            cur.close();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/iitgamingclub/iitGamingClub.fxml"));
            Stage home = new Stage();
            home.initStyle(StageStyle.UNDECORATED);
            home.setScene(new Scene(root, 1280, 720));
            home.setMaximized(true);
            home.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}