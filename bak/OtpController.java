package com.mfx.eventmanagement;

import com.mfx.eventmanagement.VerificationManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class OtpController implements Initializable {
    @FXML
    public AnchorPane root;

    @FXML
    private TextField otp1, otp2, otp3, otp4, otp5, otp6;

    @FXML
    private Button verifyButton; // Add fx:id="verifyButton" to your Button in verify.fxml if not already

    private String email; // To be set by RegController

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Restrict each OTP field to single digit (0-9)
        TextFormatter<String> digitFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d?")) { // Allow empty or one digit
                return change;
            }
            return null; // Reject change
        });

        otp1.setTextFormatter(digitFormatter);
        otp2.setTextFormatter(digitFormatter);
        otp3.setTextFormatter(digitFormatter);
        otp4.setTextFormatter(digitFormatter);
        otp5.setTextFormatter(digitFormatter);
        otp6.setTextFormatter(digitFormatter);
    }

    @FXML
    private void handleVerify() {
        // Combine OTP fields
        String otp = otp1.getText() + otp2.getText() + otp3.getText() +
                otp4.getText() + otp5.getText() + otp6.getText();

        // Error handling: Check if exactly 6 digits
        if (otp.length() != 6 || !otp.matches("\\d{6}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP", "Please enter a valid 6-digit OTP.");
            return;
        }

        // Get stored code
        String storedCode = VerificationManager.getInstance().getCode(email);
        if (storedCode == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No verification code found. It may have expired or try registering again.");
            return;
        }

        // Verify
        if (otp.equals(storedCode)) {
            VerificationManager.getInstance().removeCode(email);
            showAlert(Alert.AlertType.INFORMATION, "Success", "OTP verified successfully! Registration complete.");
            // TODO: Proceed to next screen, e.g., close window or load login.fxml
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP", "The OTP is incorrect. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}