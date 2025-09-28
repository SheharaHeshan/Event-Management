package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * Controller for the verify.fxml interface (Email Verification).
 * FIX: Replaced recursive TextFormatter focus logic with a stable textProperty listener
 * to prevent the reported infinite loop and memory leak issue.
 * DEBUG FIX: Added improved error reporting for final database insertion failure.
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
        // Retrieve the email immediately on load from the static holder
        targetEmail = UserRegistrationData.getEmail();

        // 1. Setup Input Formatters (Digit-only restriction)
        setupInputRestrictions(otp1);
        setupInputRestrictions(otp2);
        setupInputRestrictions(otp3);
        setupInputRestrictions(otp4);
        setupInputRestrictions(otp5);
        setupInputRestrictions(otp6);

        // 2. Setup Focus Listeners (Safe auto-advance logic)
        // The advance logic is separated from the TextFormatter to prevent recursion/loops.
        limitAndAdvance(otp1, otp2);
        limitAndAdvance(otp2, otp3);
        limitAndAdvance(otp3, otp4);
        limitAndAdvance(otp4, otp5);
        limitAndAdvance(otp5, otp6);
        // otp6 has no next field, no listener needed for advance.

        // 3. Display Email
        if (emailDisplay != null && targetEmail != null) {
            emailDisplay.setText(targetEmail);
        }
    }

    /**
     * Enforces the input to be a single digit.
     */
    private void setupInputRestrictions(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            // Get the new text value
            String newText = change.getControlNewText();
            // Allow empty or a single digit
            if (newText.isEmpty() || (newText.matches("\\d") && newText.length() <= 1)) {
                return change;
            }
            // Reject any other change (non-digit, too long)
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    /**
     * Listens for a single character input and safely advances focus to the next field.
     */
    private void limitAndAdvance(TextField currentField, TextField nextField) {
        currentField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() == 1 && nextField != null) {
                // Use Platform.runLater to request focus AFTER the current event cycle completes,
                // which prevents recursive events and the reported memory leak.
                Platform.runLater(nextField::requestFocus);
            }
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
            showAlert(Alert.AlertType.ERROR, "Error", "Verification code expired or not found. Please resend the code.");
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

            // --- DATABASE INSERTION ATTEMPT ---
            try {
                // Insert the full, verified record into the database
                int userId = dbManager.registerVerifiedUser(firstName, lastName, email, passwordHash);

                if (userId > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success!", "Email successfully verified. Redirecting to profile setup.");

                    // 4. Transition to Profile Setup interface
                    SceneLoader.loadScene(event, "ProfileSetup.fxml");
                } else if (userId == -2) {
                    // Check for specific error code for existing email (if DatabaseManager supports it)
                    showAlert(Alert.AlertType.ERROR, "Registration Failed", "This email is already registered.");
                } else {
                    // This handles general failure where the method returns 0 or -1
                    System.err.println("DATABASE INSERTION FAILED: registerVerifiedUser returned " + userId);
                    showAlert(Alert.AlertType.ERROR, "Registration Failed", "Verification successful, but final database insertion failed (General Error). Please check console for details.");
                }
            } catch (Exception e) {
                // Catch any unexpected runtime exceptions during the database operation
                System.err.println("CRITICAL DATABASE EXCEPTION DURING REGISTRATION:");
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "A critical database error occurred. Please check console for stack trace and contact support.");
            }
            // --- END DATABASE INSERTION ATTEMPT ---

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
    // The actual email is retrieved in initialize().
    public void initData(String email) {
        // This is now redundant since the email is retrieved from UserRegistrationData
        // but kept to prevent crashing if RegisterController still calls it.
    }
}
