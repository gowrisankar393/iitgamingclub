package com.example.iitgamingclub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IITGamingClub extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/iitgamingclub/iitGamingClub.fxml"));

        // 1. Remove the default OS Window Frame (White Title Bar)
        primaryStage.initStyle(StageStyle.UNDECORATED);
        // Optional: Allows transparent rounded corners if you want them later
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setTitle("IIT Gaming Club");

        Scene scene = new Scene(root, 1280, 720);
        // Set scene fill to transparent to support custom shapes if needed
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}