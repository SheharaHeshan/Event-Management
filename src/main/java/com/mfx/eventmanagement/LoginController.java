package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;

// NOTE: PasswordHashUtil is no longer needed since we are comparing raw strings.

/**
 * Controller for the LogIn.fxml interface.
 * Handles user authentication and navigation.
 */
public class LoginController {

    // FXML Inputs (must match fx:id in LogIn.fxml)
    @FXML private TextField mailinput;
    @FXML private PasswordField passwordinput;

    // FXML Buttons
    @FXML private MFXButton regbtn;
    @FXML private MFXButton loginbtn;

    private final DatabaseManager dbManager = new DatabaseManager();

    /**
     * Handles the button click to navigate to the Registration page (reg.fxml).
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionGoToRegister(ActionEvent event) {
        System.out.println("Transitioning from Login to Registration.");
        SceneLoader.loadScene(event, "reg.fxml");
    }

    /**
     * Implements the main user authentication logic.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void actionLogin(ActionEvent event) {
        String email = mailinput.getText().trim();
        String password = passwordinput.getText(); // Raw input password

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter both email and password.");
            return;
        }

        // 1. Retrieve user data (ID, Raw Password, Verification Status) from the database
        Map<String, Object> userData = dbManager.authenticateUser(email);

        if (userData == null) {
            // User not found in the database
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            return;
        }

        // The stored value is now the raw password string.
        String storedRawPassword = (String) userData.get("password_hash");
        int userId = (int) userData.get("user_id");
        int isVerified = (int) userData.get("is_verified");

        // 2. Verify the raw password against the stored value
        // FIX: Replaced complex hash verification with simple string comparison.
        boolean passwordMatches = password.equals(storedRawPassword);

        if (passwordMatches) {
            if (isVerified == 0) {
                showAlert(Alert.AlertType.WARNING, "Login Failed", "Account is registered but not verified. Please verify your email.");
                return;
            }

            // 3. Login Success: Store the session data
            SessionManager.setLoggedInUser(userId, email);

            // 4. Show success message and transition
            showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome back, " + email + "!");
            System.out.println("User " + userId + " logged in successfully.");

//            // Transition to the main application view
//            SceneLoader.loadScene(event, "MainFrame.fxml");
            try {
                // 1. Load the MainFrame.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
                Parent mainFrameRoot = loader.load();

                // 2. Get the MainFrameController instance
                MainFrameController mainController = loader.getController();

                // 3. Set the new Scene
                Stage stage = (Stage) ((MFXButton) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(mainFrameRoot));
                stage.show();

                // 4. Call a method on the MainFrameController to load the initial view (EventMain.fxml)
                // Note: We need to create loadInitialView() in MainFrameController
                if (mainController != null) {
                    mainController.loadEventMenu(); // Directly call the existing method
                } else {
                    System.err.println("FATAL ERROR: MainFrameController is null after loading MainFrame.fxml.");
                }

            } catch (java.io.IOException e) {
                System.err.println("Failed to load MainFrame.fxml: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "System Error", "Could not load the main application view.");
            }



        } else {
            // Password verification failed
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    /**
     * Helper method to show an alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}