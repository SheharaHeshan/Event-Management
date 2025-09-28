package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.format.DateTimeParseException;

public class AddAttendanceController {

    @FXML private TextField fullnameField;
    @FXML private TextField ageField;
    @FXML private TextField genderField;
    @FXML private TextField aboutField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private DatabaseManager dbManager = new DatabaseManager();
    private AttendanceController attendanceController;
    private int eventId;

    public void setAttendanceController(AttendanceController controller) {
        this.attendanceController = controller;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @FXML
    private void handleSave() {
        String fullname = fullnameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String gender = genderField.getText();
        String address = addressField.getText();
        String about = aboutField.getText();
        int age = 0; // Default or handle error

        try {
            age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            // TODO: Show an alert for invalid age input
            System.err.println("Invalid age format. Using 0.");
        }

        if (fullname.isEmpty() || eventId <= 0) {
            // TODO: Show an alert for mandatory fields
            System.out.println("Fullname is required.");
            return;
        }

        boolean success = dbManager.insertAttendance(
                eventId, fullname, age, gender, about, address, email, phone
        );

        if (success) {
            System.out.println("Attendance saved successfully.");
            // 1. Refresh the main Attendance ListView
            if (attendanceController != null) {
                attendanceController.refreshListView();
            }
            // 2. Close the popup
            closeWindow();
        } else {
            // TODO: Show an alert for save failure
            System.err.println("Failed to save attendance.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        // Get the current stage and close it
        Stage stage = (Stage) fullnameField.getScene().getWindow();
        stage.close();
    }
}