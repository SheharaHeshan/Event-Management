package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the ProfileSetup.fxml interface.
 * Handles extracting and storing profile data (Age, Gender, Phone, Address, Photo).
 * This controller completes the user's registration by inserting the base user
 * data (name, email, password hash) and then updating their profile in the database.
 */
public class ProfileSetupController implements Initializable {

    // FXML fields - Ensure these match the fx:id attributes in your ProfileSetup.fxml
    @FXML private Circle profileCircle;
    @FXML private MFXButton uploadPhotoButton;
    @FXML private Label photoPathLabel;
    @FXML private MFXComboBox<String> genderComboBox;
    @FXML private TextField ageField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField addressField;
    @FXML private MFXButton finishButton; // onAction="#actionFinish" in FXML

    private final DatabaseManager dbManager = new DatabaseManager();
    private String userEmail;
    private String profilePicturePath = null; // Stores the local path of the uploaded image

    /**
     * Initializes the controller, populating the ComboBox and setting the target email.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Populate Gender ComboBox
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other", "Prefer not to say"));

        // 2. Retrieve the email of the currently registering user
        // FIX: Directly use the getEmail helper to avoid array confusion and ensure it's not null.
        this.userEmail = UserRegistrationData.getEmail();

        if (this.userEmail != null) {
            System.out.println("Profile setup initialized for email: " + userEmail);
        } else {
            // This error will now only show if UserRegistrationData was never populated (e.g., bypassing registration)
            showAlert(Alert.AlertType.ERROR, "Critical Error", "User session lost. Please re-register or login.");
        }

        // Initialize photo path label
        if (photoPathLabel != null) {
            photoPathLabel.setText("No photo selected");
        }
    }

    /**
     * Handles the photo upload action. Opens a FileChooser and updates the profile circle.
     */
    @FXML
    private void actionUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Get the stage from any FXML element's scene
        // Check for null scene/window to prevent crash if not fully loaded (though rare here)
        Stage stage = (Stage) profileCircle.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Store the absolute local path.
                profilePicturePath = selectedFile.getAbsolutePath();
                if (photoPathLabel != null) {
                    photoPathLabel.setText("Photo Selected: " + selectedFile.getName());
                }

                // Display the image on the circle
                Image image = new Image(selectedFile.toURI().toString());
                profileCircle.setFill(new ImagePattern(image));

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Image Error", "Could not load image file.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the "Finish" button click. Validates input, saves profile data, and completes registration.
     */
    @FXML
    private void actionFinish(ActionEvent event) {
        // Ensure email is present
        this.userEmail = UserRegistrationData.getEmail(); // Re-fetch the current email
        if (userEmail == null || userEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "System Error", "User email context is missing. Cannot proceed.");
            return;
        }

        // Retrieve and Validate Input
        String age = ageField.getText().trim();
        String gender = genderComboBox.getSelectedValue();
        String phonenumber = phoneNumberField.getText().trim();
        String address = addressField.getText().trim();
        // Use an empty string if no photo was selected
        String profilePath = profilePicturePath != null ? profilePicturePath : "";

        // 1. Validation Check: Required fields
        if (age.isEmpty() || gender == null || phonenumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill in Age, Gender, and Phone Number.");
            return;
        }

        // 2. Validation Checks (Age, Phone Number) - Keep the previous validation logic...
        try {
            int ageInt = Integer.parseInt(age);
            if (ageInt <= 0 || ageInt > 120) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid age (1-120) as a number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Age must be a whole number.");
            return;
        }
        if (!Pattern.compile("^[0-9+\\-\\s()]{7,20}$").matcher(phonenumber).matches()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid phone number.");
            return;
        }


        // 3. Finalize Registration (One-time INSERT into the single table)

        // Retrieve base user data which is still stored
        String[] userData = UserRegistrationData.retrieveData();
        String firstName = userData[0];
        String lastName = userData[1];
        String email = userData[2];
        String passwordHash = userData[3];

        if (firstName == null || passwordHash == null) {
            showAlert(Alert.AlertType.ERROR, "System Error", "User registration data is incomplete. Please re-register.");
            return;
        }

        // CRITICAL STEP: Call the new, single method to insert ALL data
        int userId = dbManager.registerCompleteUser(
                firstName, lastName, email, passwordHash,
                age, gender, phonenumber, address, profilePath
        );

        if (userId > 0) {
            // Success: User data inserted successfully.
            // Data is cleared ONLY after final database operation succeeds.
            UserRegistrationData.retrieveAndClearData();

            showAlert(Alert.AlertType.INFORMATION, "Profile Complete", "Your profile has been successfully set up! Welcome to the Event Manager.");

            // 4. Transition to the main application view (welcome.fxml)
            SceneLoader.loadScene(event, "LogIn.fxml");
        } else {
            // Failure: Database error
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to finalize user account. Please check logs and database connection.");
            System.err.println("Database failed to finalize user registration for email: " + userEmail);
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

    // Kept to satisfy potential external calls, though email retrieval is now in initialize
    public void initData(String email) {
        this.userEmail = email;
    }
}