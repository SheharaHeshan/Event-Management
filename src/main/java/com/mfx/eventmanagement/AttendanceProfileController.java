package com.mfx.eventmanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.format.DateTimeFormatter;

public class AttendanceProfileController {

    // FXML fields matching the fx:id's in AttendanceProfile.fxml (assuming standard labels based on data model)
    @FXML private Label AttenName;
    @FXML private Label AttenAge;
    @FXML private Label AttenGender;
    @FXML private Label AttenNumber;
    @FXML private Label AttenAddress;
    @FXML private Label AttenAbout;

    @FXML private ImageView profilePicture; // Optional: If you have an ImageView for profile picture


    /**
     * Public method to set the full attendance record data.
     * @param record The full AttendanceRecordStore object.
     */
    public void setAttendanceRecord(AttendanceRecordStore record) {
        if (record != null) {
            // Set basic info
            AttenName.setText(record.getFullname());
            AttenAge.setText(String.valueOf(record.getAge()));
            AttenGender.setText(record.getGender());
            AttenAbout.setText(record.getAbout());
            AttenAddress.setText(record.getAddress());
            AttenNumber.setText(record.getPhonenumber());

            // Optional: Set profile picture if path is available
            if (profilePicture != null && record.getProfilePicturePath() != null && !record.getProfilePicturePath().isEmpty()) {
                try {
                    Image image = new Image("file:" + record.getProfilePicturePath());
                    profilePicture.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading profile picture: " + e.getMessage());
                }
            }
        }
    }
}