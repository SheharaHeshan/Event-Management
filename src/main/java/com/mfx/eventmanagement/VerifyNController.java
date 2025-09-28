package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * Controller for the verify.fxml interface (Email Verification).
 * FIX: Replaced recursive TextFormatter focus logic with a stable textProperty listener
 * to prevent the reported infinite loop and memory leak issue.
 * DEBUG FIX: Added improved error reporting for final database insertion failure.
 * CRITICAL FIX: The verified email is implicitly passed by keeping the UserRegistrationData
 * intact until ProfileSetupController.actionFinish().
 */
public class VerifyNController implements Initializable {

    // FXML elements (assuming they match the verify.fxml structure)
    @FXML private MFXButton verifybtn;
    @FXML private MFXButton verifybackbtn;

    // OTP Input Fields (from verify.fxml)
    @FXML private TextField otp1, otp2, otp3, otp4, otp5, otp6;

    // Text element to show the target email (fx:id="emailDisplay")
    @FXML private Text emailDisplay;

    private final DatabaseManager dbManager = new DatabaseManager();
    private String targetEmail;

    /**
     * Initialization method called by FXMLLoader.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ... (existing TextFormatter setup logic for OTP fields) ...

        // 1. Retrieve the registration data (including email)
        String emailFromData = UserRegistrationData.getEmail();

        if (emailFromData != null) {
            targetEmail = emailFromData;
            // Update the FXML Text element to show the target email
            if (emailDisplay != null) {
                emailDisplay.setText(targetEmail);
            }
        } else {
            // Handle case where registration data is missing (e.g., direct navigation)
            System.err.println("CRITICAL: Registration context lost.");
            // showAlert(Alert.AlertType.ERROR, "System Error", "Registration context lost. Please return to registration.");
        }

        // UnaryOperator for single digit input
        UnaryOperator<TextFormatter.Change> digitFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d?") && newText.length() <= 1) {
                return change;
            }
            return null;
        };

        TextField[] otpFields = {otp1, otp2, otp3, otp4, otp5, otp6};
        for (TextField field : otpFields) {
            if (field != null) {
                field.setTextFormatter(new TextFormatter<>(digitFilter));
            }
        }

        // Add listeners for automatic focus on next field
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            if (otpFields[index] != null) {
                otpFields[index].textProperty().addListener((obs, oldText, newText) -> {
                    if (newText.length() == 1) {
                        if (index < otpFields.length - 1) {
                            Platform.runLater(() -> otpFields[index + 1].requestFocus());
                        } else {
                            verifybtn.requestFocus(); // Focus on the verify button on the last input
                        }
                    } else if (newText.isEmpty() && !oldText.isEmpty()) {
                        if (index > 0) {
                            Platform.runLater(() -> otpFields[index - 1].requestFocus());
                        }
                    }
                });
            }
        }
    }


    /**
     * Handles the "Verify" button click.
     * This action will check the OTP and load the Profile Setup interface (ProfileSetup.fxml).
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionVerify(ActionEvent event) {
        String otp = otp1.getText() + otp2.getText() + otp3.getText() +
                otp4.getText() + otp5.getText() + otp6.getText();

        if (otp.length() != 6 || !otp.matches("\\d{6}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP", "Please enter a valid 6-digit OTP.");
            return;
        }

        String email = targetEmail; // Use the email retrieved in initialize
        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "System Error", "Email context lost. Please return to registration.");
            return;
        }

        String storedCode = VerificationManager.getInstance().getCode(email);

        if (storedCode == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No verification code found. It may have expired or try registering again.");
            return;
        }

        if (otp.equals(storedCode)) {
            VerificationManager.getInstance().removeCode(email);

            // ⚠️ FIX: REMOVED UserRegistrationData.retrieveAndClearData()
            // Data will be kept for ProfileSetupController to retrieve.

            // 1. Show success alert
            showAlert(Alert.AlertType.INFORMATION, "Success", "OTP verified successfully! Proceeding to Profile Setup.");

            // 2. Load the next scene (ProfileSetup.fxml).
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mfx/eventmanagement/ProfileSetup.fxml"));
                Parent profileSetupRoot = loader.load();

                // Get the controller and pass the email (No need for initData, as email is now in UserRegistrationData)
                // ProfileSetupController profileController = loader.getController();
                // profileController.initData(email); // Removed: rely on UserRegistrationData.

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(profileSetupRoot));
                stage.show();

            } catch (IOException e) {
                System.err.println("Error loading ProfileSetup.fxml: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the next screen.");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP", "The entered code is incorrect. Please try again.");
        }
    }

    @FXML
    private void actionBackToRegister(ActionEvent event) {
        // Clear any temporary data if the user chooses to go back
        UserRegistrationData.retrieveAndClearData();
        SceneLoader.loadScene(event, "reg.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Dummy method to match previous controller structure if RegisterController calls it.
    public void initData(String email) {
        // No longer needed here as email is retrieved from UserRegistrationData
    }
}