package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

public class EventCardController {

    @FXML private Label EventLabel;
    @FXML private Label StartDateLabel;
    @FXML private Label StartTimeLabel;
    @FXML private Label EndDateLabel;
    @FXML private Label EndTimeLabel;
    @FXML private Label AttendenceLable;
    @FXML private Label StatusLable;
    @FXML private MFXButton EditBtn;
    @FXML private ImageView imgedit;
    @FXML private MFXButton DelBtn;
    @FXML private ImageView imgdel;

    @FXML private  MFXButton SheduleEventBtn;

    private EventDataStore event; // Store the event data internally
    private MainFrameController mainFrameController; // Reference to the main window controller



    /**
     * Sets the event data onto the card's labels.
     *
     * @param event               The Event data model object.
     * @param mainFrameController
     */
    public void setEventData(EventDataStore event, MainFrameController mainFrameController) {

        this.event = event; // Store the data
        this.mainFrameController = mainFrameController; // Store the MainFrameController reference

        EventLabel.setText(event.getName());
        StartDateLabel.setText(event.getStartDate().toString());
        StartTimeLabel.setText(event.getStartTime().toString());
        EndDateLabel.setText(event.getEndDate().toString());
        EndTimeLabel.setText(event.getEndTime().toString());
        AttendenceLable.setText(event.getAttendanceType());
        // For Status, you'd typically calculate it based on dates or set it manually
        StatusLable.setText("Scheduled"); // Placeholder status
    }

    /**
     * Handles the action when the Schedule button is clicked.
     */
    @FXML
    private void handleScheduleButtonAction(ActionEvent event) {
        try {
            if (mainFrameController != null) {
                // This will execute once the assignment is fixed
                System.out.println("✅ MainFrameController found. Calling loadScheduleView for event: " + this.event.getName());
                // Tell the main controller to load the calendar view for this event
                mainFrameController.loadScheduleView(this.event);
            } else {
                // This is the message you will see if the assignment is still incorrect
                System.err.println("❌ ERROR: MainFrameController is NULL in EventCardController. Cannot load schedule view.");
            }
        } catch (Exception e) {
            System.err.println("An unexpected exception occurred during schedule action:");
            e.printStackTrace();
        }
    }


}
