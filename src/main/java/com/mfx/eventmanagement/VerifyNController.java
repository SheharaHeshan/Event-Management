package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the verify.fxml interface (Email Verification).
 * Handles OTP input, verification, final database insertion, and transition to Profile Setup.
 */
public class VerifyNController implements Initializable {

    // FXML elements (assuming they match the verify.fxml structure)
    @FXML private MFXButton verifybtn;
    @FXML private MFXButton verifybackbtn;

    // OTP Input Fields (from verify.fxml)
    @FXML private TextField otp1, otp2, otp3, otp4, otp5, otp6;

    // Text element to show the target email (fx:id="emailDisplay" added to FXML)
    @FXML private Text emailDisplay;

    private final DatabaseManager dbManager = new DatabaseManager();
    private String targetEmail;

    /**
     * Called by RegisterController to ensure the email is displayed.
     */
    public void initData(String email) {
        this.targetEmail = email;
        if (emailDisplay != null) {
            emailDisplay.setText(email);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrieve the email immediately on load from the static holder
        targetEmail = UserRegistrationData.getEmail();

        // Setup input formatters for auto-advance and single digit
        otp1.setTextFormatter(createDigitFormatter(otp2));
        otp2.setTextFormatter(createDigitFormatter(otp3));
        otp3.setTextFormatter(createDigitFormatter(otp4));
        otp4.setTextFormatter(createDigitFormatter(otp5));
        otp5.setTextFormatter(createDigitFormatter(otp6));
        otp6.setTextFormatter(createDigitFormatter(null));

        // Ensure email display is set if data is available
        if (emailDisplay != null && targetEmail != null) {
            emailDisplay.setText(targetEmail);
        }
    }

    /**
     * Helper to create a TextFormatter that also handles auto-advancing the focus.
     */
    private TextFormatter<String> createDigitFormatter(TextField nextField) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 1 || (newText.length() == 1 && !newText.matches("\\d"))) {
                return null;
            }

            if (newText.length() == 1 && nextField != null) {
                // Advance focus on input
                javafx.application.Platform.runLater(nextField::requestFocus);
            }
            return change;
        });
    }

    // --- Action Handler ---

    @FXML
    private void actionVerify(ActionEvent event) {
        if (targetEmail == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Target email missing. Please re-register.");
            return;
        }

        // 1. Combine OTP fields
        String enteredOtp = otp1.getText() + otp2.getText() + otp3.getText() +
                otp4.getText() + otp5.getText() + otp6.getText();

        if (enteredOtp.length() != 6 || !enteredOtp.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP", "Please enter a valid 6-digit code.");
            return;
        }

        // 2. Check OTP against VerificationManager
        String storedCode = VerificationManager.getInstance().getCode(targetEmail);

        if (storedCode == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Verification code expired or not found. Please click 'Resend Code'.");
            return;
        }

        if (enteredOtp.equals(storedCode)) {
            // OTP SUCCESS! Remove code immediately.
            VerificationManager.getInstance().removeCode(targetEmail);

            // 3. Finalize Registration: Retrieve all data
            String[] userData = UserRegistrationData.retrieveAndClearData();

            if (userData[0] == null || userData[2] == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Missing user registration data. Please re-register.");
                return;
            }

            String firstName = userData[0];
            String lastName = userData[1];
            String email = userData[2];
            String passwordHash = userData[3];

            // Insert the full, verified record into the database
            int userId = dbManager.registerVerifiedUser(firstName, lastName, email, passwordHash);

            if (userId > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success!", "Email successfully verified. You can now log in or complete your profile.");

                // 4. Transition to Profile Setup interface
                SceneLoader.loadScene(event, "ProfileSetup.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Verification successful, but final database insertion failed. Please contact support.");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP", "The entered code is incorrect. Please try again.");
        }
    }

    @FXML
    private void actionBackToRegister(ActionEvent event) {
        System.out.println("Returning from Verification to Registration.");
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
}
