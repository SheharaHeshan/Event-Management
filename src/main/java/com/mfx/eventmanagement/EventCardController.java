package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


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

    /**
     * Sets the event data onto the card's labels.
     * @param event The Event data model object.
     */
    public void setEventData(EventDataStore event) {
        EventLabel.setText(event.getName());
        StartDateLabel.setText(event.getStartDate().toString());
        StartTimeLabel.setText(event.getStartTime().toString());
        EndDateLabel.setText(event.getEndDate().toString());
        EndTimeLabel.setText(event.getEndTime().toString());
        AttendenceLable.setText(event.getAttendanceType());
        // For Status, you'd typically calculate it based on dates or set it manually
        StatusLable.setText("Scheduled"); // Placeholder status
    }

}
