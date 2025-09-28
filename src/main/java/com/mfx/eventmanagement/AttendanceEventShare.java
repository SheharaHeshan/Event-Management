package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

public class AttendanceEventShare {

    @FXML
    private TextField shareUrlField; // Add fx:id="shareUrlField" to the TextField in AttendanceEventShare.fxml
    @FXML
    private MFXButton copyButton; // Add fx:id="copyButton" to the Copy button

    private static final String VPS_BASE_URL = "https://your-subdomain.com/attendance";

    private int eventId;

    public void setEventId(int eventId) {
        this.eventId = eventId;
        // Generate the URL immediately when the ID is set
        String shareableUrl = VPS_BASE_URL + "?event_id=" + eventId;
        shareUrlField.setText(shareableUrl);
    }

    @FXML
    private void handleCopyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(shareUrlField.getText());
        clipboard.setContent(content);

        // Optional: Provide visual feedback (e.g., change button text temporarily)
        System.out.println("URL copied to clipboard: " + shareUrlField.getText());
    }
}