package com.mfx.eventmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Objects;

/**
 * Utility class to handle the loading and switching of FXML scenes.
 * This centralizes the navigation logic, making controllers cleaner.
 */
public class SceneLoader {

    /**
     * Switches the current scene to a new FXML file.
     * @param event The ActionEvent from the button click, used to get the current window (Stage).
     * @param fxmlFileName The name of the FXML file to load (e.g., "Login.fxml").
     */
    public static void loadScene(ActionEvent event, String fxmlFileName) {
        try {
            // Get the stage (window) from the source of the event
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Load the FXML file
            // Note: We use Objects.requireNonNull for resource loading security
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneLoader.class.getResource("/com/mfx/eventmanagement/" + fxmlFileName)));

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Handle file loading errors gracefully
            System.err.println("Error loading FXML scene: " + fxmlFileName);
            e.printStackTrace();
            // Optionally show an error message to the user
        } catch (NullPointerException e) {
            System.err.println("FXML file not found or is null: " + fxmlFileName);
            e.printStackTrace();
        }
    }
}