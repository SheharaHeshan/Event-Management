package com.mfx.eventmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point for the Event Management JavaFX Application.
 * This class initializes the primary stage and loads the initial welcome scene.
 */
public class RegMain extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method returns,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Load the initial FXML file (welcome.fxml)
            // The path assumes the FXML file is located in the resources folder
            // under com/mfx/eventmanagement/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mfx/eventmanagement/welcome.fxml"));
            Parent root = loader.load();

            // 2. Setup the Scene
            Scene scene = new Scene(root);

            // 3. Configure the Primary Stage (Window)
            primaryStage.setTitle("Event Management System");
            // Set the initial size based on your FXML file (1024x768)
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Failed to load welcome.fxml or related resource.");
            e.printStackTrace();
            // Exit the application if the initial load fails
            System.exit(1);
        }
    }

    /**
     * The main method, which is the standard Java entry point.
     * It calls the launch() method inherited from Application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}