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

        //remove the default windows title bar
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //make the stage transparent as it helps to add my own elements without interfering
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //window name
        primaryStage.setTitle("IIT Gaming Club");

        Scene scene = new Scene(root, 1280, 720);
        //setting screen color as transparent
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}