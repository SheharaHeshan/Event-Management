package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the welcome.fxml interface.
 * Handles the initial navigation to the login screen.
 */
public class WelcomeController {

    @FXML
    private MFXButton startbtn; // fx:id of the "Get Started" button

    /**
     * Handles the "Get Started" button click.
     * Loads the LogIn.fxml interface.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionGetStarted(ActionEvent event) {
        // Log the transition for debugging
        System.out.println("Transitioning from Welcome to Login.");
        // Use the centralized SceneLoader utility
        SceneLoader.loadScene(event, "LogIn.fxml");
    }
}