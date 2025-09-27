package com.mfx.eventmanagement;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class verifycontroller {

    @FXML
    private TextField mailinput;
    @FXML
    private TextField otp1;
    @FXML
    private TextField otp2;
    @FXML
    private TextField otp3;
    @FXML
    private TextField otp4;
    @FXML
    private TextField otp5;
    @FXML
    private TextField otp6;
    @FXML
    private MFXButton verifybtn;
    @FXML
    private MFXButton continuebtn;


    private static final Logger LOGGER = Logger.getLogger(verifycontroller.class.getName());



    @FXML
    private void actioncontinue() {
        String email = mailinput.getText().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        String code = generateCode();
        // Store code in shared manager
        VerificationManager.getInstance().addCode(email, code);

        // Switch to verify.fxml immediately
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/verify.fxml"));
            Parent root = loader.load();
            OtpController otpController = loader.getController();
            otpController.setEmail(email); // Pass email to OtpController

            Stage stage = (Stage) continuebtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Email Verification");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load verify.fxml", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load verification screen.");
            continuebtn.setDisable(false);
            VerificationManager.getInstance().removeCode(email); // Clean up
            return;
        }

        continuebtn.setDisable(true);
// Run email sending in background thread
        Task<Boolean> emailTask = new Task<>() {
            @Override
            protected Boolean call() {
                try {
                    return EmailSender.sendVerificationEmail(email, code);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to send email to " + email, e);
                    return false;
                }
            }
        };

        emailTask.setOnSucceeded(event -> {
            continuebtn.setDisable(false);
            boolean sent = emailTask.getValue();
            if (sent) {
                showAlert(Alert.AlertType.INFORMATION, "Code Sent", "Verification code sent to " + email + ". Check your inbox or spam.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Email Error", "Failed to send verification email. Check your connection or credentials.");
                VerificationManager.getInstance().removeCode(email);
            }
        });

        emailTask.setOnFailed(event -> {
            continuebtn.setDisable(false);
            LOGGER.log(Level.SEVERE, "Email task failed", emailTask.getException());
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while sending the email.");
            VerificationManager.getInstance().removeCode(email);
        });

        new Thread(emailTask).start();
    }

    private String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6-digit code
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$"); // Basic regex validation
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



