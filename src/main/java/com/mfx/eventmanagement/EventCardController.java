package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller class for a single event card UI component.
 * This class handles populating the card with event data and managing its actions.
 */
public class EventCardController {

    @FXML
    private VBox eventCard;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventDescriptionLabel;
    @FXML
    private Label eventDatesLabel;
    @FXML
    private Label eventTypeLabel;
    @FXML
    private Circle statusCircle;
    @FXML
    private MFXButton scheduleButton;

    // A formatter to display the dates in a user-friendly format
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    /**
     * Sets the data for the event card's UI elements.
     * This method is called by EventCardCell to populate the card.
     *
     * @param name The name of the event.
     * @param description The description of the event.
     * @param startDate The start date of the event.
     * @param endDate The end date of the event.
     * @param type The type of the event.
     * @param isActive A flag to determine if the event is active (green) or not (red).
     */
    public void setEventData(String name, String description, LocalDate startDate, LocalDate endDate, String type, boolean isActive) {
        eventNameLabel.setText(name);
        eventDescriptionLabel.setText(description);
        eventDatesLabel.setText(startDate.format(DATE_FORMATTER) + " - " + endDate.format(DATE_FORMATTER));
        eventTypeLabel.setText(type);

        if (isActive) {
            statusCircle.setStyle("-fx-fill: green;");
        } else {
            statusCircle.setStyle("-fx-fill: red;");
        }
    }

    /**
     * Handles the action for the "Schedule" button on the card.
     * This method is triggered when the button is clicked.
     */
    @FXML
    private void handleScheduleButton() {
        System.out.println("Schedule button clicked for: " + eventNameLabel.getText());
        // You would add your scheduling logic here.
        // For example, opening a scheduling window or a confirmation dialog.
    }
}