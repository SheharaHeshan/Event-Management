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

import java.time.LocalDate;

/**
 * Controller class for the event creation popup window.
 * This class handles all the user interactions and logic for the event creation form.
 */
public class EventCreate {

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
    private MFXButton eventCreatebtn;

    // A reference to the MainFrame controller to pass the new event data
    private MainFrame mainFrameController;

    /**
     * Sets the reference to the main controller. This method is called by MainFrame.
     * @param mainFrame The MainFrame controller instance.
     */
    public void setMainFrameController(MainFrame mainFrame) {
        this.mainFrameController = mainFrame;
    }

    /**
     * Initializes the controller. This method is called after the FXML has been loaded.
     */
    @FXML
    public void initialize() {
        // Populate the ComboBox with event types
        eventTypeComboBox.getItems().addAll("Work", "Social", "Training", "Other");
    }

    /**
     * Handles the action for the "Create Event" button.
     * This method extracts data from the form fields, creates a new event,
     * and passes it to the main controller.
     */
    @FXML
    private void handleCreateButton(ActionEvent event) {
        // 1. Extract data from the form fields
        String name = Eventname.getText();
        LocalDate startDate = startDatePicker.getCurrentDate();
        LocalDate endDate = endDatePicker.getCurrentDate();
        String eventType = String.valueOf(eventTypeComboBox.getRotate());

        // Basic validation: Check if required fields are filled
        if (name.isEmpty() || startDate == null || endDate == null || eventType == null) {
            // For now, we will print an error. In a final application, you would show a popup.
            System.err.println("Validation Error: Please fill in all required fields.");
            return;
        }

        // 2. Create a new Event object using the extracted data
        MainFrame.Event newEvent = new MainFrame.Event(name, eventType, startDate, endDate);

        // 3. Pass the new event to the MainFrame controller
        if (mainFrameController != null) {
            mainFrameController.addEventToList(newEvent);
        } else {
            System.err.println("MainFrame controller not set. Cannot add event.");
        }

        // 4. Close the popup window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
