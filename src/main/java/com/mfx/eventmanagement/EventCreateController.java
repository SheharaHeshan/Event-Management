package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalTimeTextField;
import javafx.scene.control.Alert;
import jfxtras.scene.control.LocalTimeTextField; // New Import
import java.time.LocalTime; // New Import
import java.time.LocalDate;


/**
 * Controller class for the event creation popup window.
 * This class handles all the user interactions and logic for the event creation form.
 */
public class EventCreateController {

    @FXML
    private TextField Eventname;

    @FXML
    private TextArea descriptionArea;



    @FXML
    private MFXDatePicker startDatePicker;

    @FXML
    private MFXDatePicker endDatePicker;

    @FXML
    private MFXComboBox<String> eventTypeComboBox;

    @FXML
    private MFXComboBox<String> attendenceTypeComboBox;

    @FXML
    private MFXButton eventCreatebtn;

    @FXML
    private LocalTimeTextField startTimeField;

    @FXML
    private LocalTimeTextField endTimeField;


    private DatabaseManager dbManager = new DatabaseManager(); // Initialize DB Manager
    // A reference to the MainFrame controller to pass the new event data
    private MainFrameController  MainFrame ;


    public void setMainFrameController(MainFrameController mainFrame) {
        this.MainFrame = mainFrame;
    }

    // Holds the event data upon successful creation
    private EventDataStore createdEvent;


    /**
     * Initializes the controller. This method is called after the FXML has been loaded.
     */
    @FXML
    public void initialize() {
        // Populate the ComboBox with event types
        eventTypeComboBox.getItems().addAll("Physical", "Online", "Hybrid");
        attendenceTypeComboBox.getItems().addAll("Open","VIP","Invite-only","Ticketed");
    }

    /**
     * Handles the action for the "Create Event" button.
     * This method extracts data from the form fields, creates a new event,
     * and passes it to the main controller.
     */
    @FXML
    private void handleCreateButton(ActionEvent event) {

        // 1. Collect Data from UI
        String name = Eventname.getText();
        String description = descriptionArea.getText();
        LocalDate startDate = startDatePicker.getDate();
        LocalDate endDate = endDatePicker.getDate();
        LocalTime startTime = startTimeField.getLocalTime();
        LocalTime endTime = endTimeField.getLocalTime();
        String eventType = eventTypeComboBox.getSelectedValue();
        String attendanceType = attendenceTypeComboBox.getSelectedValue();

        // 2. Basic Validation (you should add more robust validation)
        if (name.isEmpty() || startDate == null || endDate == null || startTime == null || endTime == null || eventType == null || attendanceType == null) {
            showAlert("Validation Error", "Please fill in all required fields.", Alert.AlertType.ERROR);
            return;
        }

        // --- NEW STEP: Create the Event Object here ---
        EventDataStore newEvent = new EventDataStore(name, description, startDate, endDate, startTime, endTime, eventType, attendanceType);

        // 3. Insert Data into Database
        boolean success = dbManager.insertNewEvent(
                name, description, startDate, endDate, startTime, endTime, eventType, attendanceType);

        // 4. Provide Feedback and Close
        if (success) {
            showAlert("Success", "Event '" + name + "' created successfully!", Alert.AlertType.INFORMATION);
            // Optionally, you can refresh the main frame here:
            // if (MainFrame != null) MainFrame.refreshEventList();

            // FIX: Set the createdEvent object
            this.createdEvent = newEvent;

        } else {
            showAlert("Error", "Failed to create event. Check database connection/logs.", Alert.AlertType.ERROR);
            return; // Don't close window on error
        }

        // 4. Close the popup window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();


    }


    // --- NEW GETTER METHOD ---
    public EventDataStore getCreatedEvent() {
        return createdEvent;
    }



    // Helper method to display alerts
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Handles the action for the "Cancel" button.
     * This method closes the popup window without creating an event.
     */
    @FXML
    private void handleCancelButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}