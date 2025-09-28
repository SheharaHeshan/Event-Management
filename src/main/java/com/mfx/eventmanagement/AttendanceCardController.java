package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;

public class AttendanceCardController {

    // FXML fields matching the fx:id's in AttendanceCard.fxml
    @FXML private Label cardName;
    @FXML private Label cardEmail;
    @FXML private Label cardDate;
    @FXML private Label cardTime;
    @FXML private MFXButton editbtn; // For the action button
    @FXML private MFXButton delbtn;

    // Define the formatters for date and time display
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Public method called by the ListCell to set data.
     * @param record The AttendanceRecordStore object for this cell.
     */
    public void setAttendanceRecord(AttendanceRecordStore record) {
        if (record != null) {
            // 1. Set Name and Email
            cardName.setText(record.getFullname());
            cardEmail.setText(record.getEmail());

            // 2. Set Formatted Date and Time from the logTimestamp
            cardDate.setText(record.getLogTimestamp().format(DATE_FORMAT));
            cardTime.setText(record.getLogTimestamp().format(TIME_FORMAT));

            // Optional: Set up actions here
            // editbtn.setOnAction(e -> handleEditAction(record));
        }
    }

    // Optional: Add action handlers here if needed, e.g., handleEditAction
}