package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.regex.Pattern;

/**
 * Controller for the reg.fxml interface (Registration Part 1).
 * Handles data validation, stores data temporarily, sends OTP via EmailSender,
 * and transitions to the email verification scene (VerifyNController).
 */
public class RegisterController {

    // FXML elements mapped from reg.fxml (Matching fx:id from your FXML)
    @FXML private AnchorPane rootPane;
    @FXML private MFXButton regcontinuebtn;
    @FXML private MFXButton regbackbtn;

    // INPUT FIELDS (MATCHING fx:id IN reg.fxml)
    @FXML private TextField fnamex;
    @FXML private TextField snamex;
    @FXML private TextField mailinputx;
    @FXML private PasswordField pass;
    @FXML private PasswordField confirmpass;
    @FXML private CheckBox agreecheck;

    private final DatabaseManager dbManager = new DatabaseManager();

    @FXML
    public void initialize() {
        regcontinuebtn.setDisable(true);
        agreecheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            regcontinuebtn.setDisable(!newValue);
        });
    }

    // --- Utility Methods (hashPassword and showAlert remain the same) ---

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found or failed during hashing.");
            return null;
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String validateInputs() {
        String fname = fnamex.getText().trim();
        String sname = snamex.getText().trim();
        String email = mailinputx.getText().trim();
        String password = pass.getText();
        String confirmPassword = confirmpass.getText();

        if (fname.isEmpty() || sname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "All fields must be filled out.";
        }
        // ... (rest of validation logic remains the same)
        if (!Pattern.matches("^[a-zA-Z\\s]{2,}$", fname) || !Pattern.matches("^[a-zA-Z\\s]{2,}$", sname)) {
            return "First Name and Last Name must contain only letters and be at least 2 characters long.";
        }
        if (!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", email)) {
            return "Please enter a valid email address (e.g., user@domain.com).";
        }
        if (!password.equals(confirmPassword)) {
            return "Password and Confirm Password must match.";
        }
        if (password.length() < 8 || !Pattern.matches(".*[A-Z].*", password) ||
                !Pattern.matches(".*[a-z].*", password) || !Pattern.matches(".*[0-9].*", password) ||
                !Pattern.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*", password)) {
            return "Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character.";
        }

        return null;
    }

    // --- Action Handler ---

    @FXML
    private void actionContinue(ActionEvent event) {
        // 1. Run Validation
        String validationError = validateInputs();
        if (validationError != null) {
            showAlert(AlertType.ERROR, "Input Error", validationError);
            return;
        }

        String email = mailinputx.getText().trim();
        String rawPassword = confirmpass.getText();
        String firstName = fnamex.getText().trim();
        String lastName = snamex.getText().trim();

        // 2. Check if the email already exists in the database
        if (dbManager.isEmailRegistered(email)) {
            showAlert(AlertType.ERROR, "Registration Failed", "The email address '" + email + "' is already registered. Please login or use a different email.");
            return;
        }

        // 3. Hash the password and store unverified data temporarily
        String passwordHash = hashPassword(rawPassword);
        if (passwordHash == null) {
            showAlert(AlertType.ERROR, "Security Error", "Could not process password securely. Registration stopped.");
            return;
        }
        UserRegistrationData.storeData(firstName, lastName, email, passwordHash);

        // 4. Generate and send OTP
        String otpCode = UserRegistrationData.generateOtp();
        VerificationManager.getInstance().addCode(email, otpCode);

        // Use a new thread for email sending to prevent blocking the JavaFX UI
        new Thread(() -> {
            boolean success = EmailSender.sendVerificationEmail(email, otpCode);
            if (!success) {
                // If email fails, inform the user via the UI thread
                javafx.application.Platform.runLater(() ->
                        showAlert(AlertType.ERROR, "Email Send Failed", "Could not send verification email. Please check the email address and your connection.")
                );
            }
        }).start();

        // 5. Transition to VerifyNController
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mfx/eventmanagement/verify.fxml"));
            Parent root = loader.load();

            // Pass the email to the next controller for display purposes
            VerifyNController verifyController = loader.getController();
            // Since the email is already in the static holder, we can just trigger display logic
            verifyController.initData(email);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Scene Error", "Could not load verification screen.");
        }
    }

    @FXML
    private void actionBackToLogin(ActionEvent event) {
        System.out.println("Returning from Registration to Login.");
        SceneLoader.loadScene(event, "LogIn.fxml");
    }
}