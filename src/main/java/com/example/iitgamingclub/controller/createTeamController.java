package com.example.iitgamingclub.controller;

import com.example.iitgamingclub.model.Participant;
import com.example.iitgamingclub.model.Team;
import com.example.iitgamingclub.service.DataManager;
import com.example.iitgamingclub.service.TeamMatchingService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class createTeamController {
    @FXML private TextField txtTeamSize;
    @FXML private ComboBox<String> cmbFiles;
    @FXML private TextArea txtOutput;

    @FXML public void initialize() {
        DataManager dm = DataManager.getInstance();
        cmbFiles.getItems().addAll(dm.getLoadedFilePaths());
        if (dm.getCurrentActiveFile() != null) cmbFiles.getSelectionModel().select(dm.getCurrentActiveFile());
    }

    @FXML public void generate() {
        if (cmbFiles.getValue() == null) {
            txtOutput.setText("No CSV file selected.");
            return;
        }
        try {
            int n = Integer.parseInt(txtTeamSize.getText());
            if (n <= 0) throw new NumberFormatException();

            List<Participant> pool = DataManager.getInstance().getParticipants(cmbFiles.getValue());
            TeamMatchingService service = new TeamMatchingService();
            List<Team> teams = service.generateTeams(pool, n);

            StringBuilder sb = new StringBuilder();
            for (Team t : teams) sb.append(t.toString()).append("\n");
            txtOutput.setText(sb.toString());

        } catch (NumberFormatException e) {
            txtOutput.setText("Enter a valid team size.");
        }
    }
}