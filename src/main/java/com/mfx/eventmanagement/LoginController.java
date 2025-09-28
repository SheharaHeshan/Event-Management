package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

// Assuming the FXML file is named LogIn.fxml and the controller is com.mfx.eventmanagement.LoginController
// You will need to update the fx:controller in your LogIn.fxml to match this class.

/**
 * Controller for the LogIn.fxml interface.
 * Handles navigation to the registration screen.
 */
public class LoginController {

    // You will add @FXML fields for email/password and the login button later.
    @FXML private MFXButton regbtn;
    @FXML private MFXButton loginbtn;


    /**
     * Handles the button click to navigate to the Registration page (reg.fxml).
     * This is triggered by the "Register" button or a similar link/button.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionGoToRegister(ActionEvent event) {
        // Log the transition for debugging
        System.out.println("Transitioning from Login to Registration.");
        SceneLoader.loadScene(event, "reg.fxml");
    }

    /**
     * Placeholder for the main login action.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionLogin(ActionEvent event) {
        // TODO: Implement user authentication logic here
        System.out.println("Attempting Login...");
    }
}
