package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

// You will need to update the fx:controller in your ProfileSetup.fxml to match this class.

/**
 * Controller for the ProfileSetup.fxml interface.
 * Handles the "Finish" (to login) and "Back" (to verification) transitions.
 */
public class ProfileSetupController {

    // You will add @FXML fields for gender, age, phone number, address, etc. here later.
    @FXML private MFXButton finishbtn;
    @FXML private MFXButton profileuplaodbtn;
    @FXML private MFXButton regbackbtn;

    /**
     * Handles the "Finish" button click.
     * This action will save the remaining profile data (to be implemented)
     * and loads the LogIn.fxml interface for the user to sign in.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionFinish(ActionEvent event) {
        // TODO: Implement saving of profile setup data (gender, age, phone, address, etc.)
        System.out.println("Profile Setup complete. Transitioning to Login.");
        SceneLoader.loadScene(event, "LogIn.fxml");
    }

    /**
     * Handles the "Back" button click.
     * Returns the user to the Verification interface (verify.fxml).
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionBackToVerify(ActionEvent event) {
        System.out.println("Returning from Profile Setup to Verification.");
        SceneLoader.loadScene(event, "verify.fxml");
    }
}