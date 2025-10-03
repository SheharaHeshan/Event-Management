package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

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
    private AnchorPane cardPane; // Reference to this card's AnchorPane
    private MFXListView<AnchorPane> parentListView; // Reference to the parent list view



    /**
     * Sets the event data onto the card's labels.
     *
     * @param event               The Event data model object.
     * @param mainFrameController
     */
    public void setEventData(EventDataStore event, MainFrameController mainFrameController , AnchorPane cardPane, MFXListView<AnchorPane> parentListView) {

        this.event = event; // Store the data
        this.mainFrameController = mainFrameController; // Store the MainFrameController reference
        this.cardPane = cardPane; // Store the card's AnchorPane
        this.parentListView = parentListView; // Store the parent list view

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

    @FXML
    private void handleDeleteButtonAction(ActionEvent actionEvent){
       try {
           // Step 1: Get the event ID
           int eventId = event.getEventId();

           // Step 2: Remove the card from the MFXListView
           if (parentListView != null && cardPane != null) {
               parentListView.getItems().remove(cardPane);
               System.out.println("✅ Event card removed from list view for event: " + event.getName());
           } else {
               System.err.println("❌ ERROR: Cannot remove card. Parent list view or card pane is null.");
           }

           // Step 3: Delete the event from the database
           DatabaseManager dbManager = new DatabaseManager();
           boolean deleted = dbManager.deleteEventById(eventId);
           if (deleted) {
               System.out.println("✅ Event with ID " + eventId + " deleted from database.");
           } else {
               System.err.println("❌ ERROR: Failed to delete event with ID " + eventId + " from database.");
           }



       } catch (Exception e) {
           System.err.println("An unexpected exception occurred during edit (delete) action:");
           e.printStackTrace();
       }
    }


}