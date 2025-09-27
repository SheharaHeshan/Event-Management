package com.mfx.eventmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class verifymain extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the registration FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(verifymain.class.getResource("reg.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // Adjust size as needed

        // Set up the stage
        primaryStage.setTitle("Event Management App - Registration");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Optional: Lock window size
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}