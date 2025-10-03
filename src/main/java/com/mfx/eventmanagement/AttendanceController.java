package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class AttendanceController {

    @FXML
    private MFXComboBox<String> eventComboBox;
    @FXML
    private MFXButton addManuallyButton;
    @FXML
    private MFXButton shareButton;
    @FXML
    private MFXListView<AttendanceRecordStore> attendanceListView;
    @FXML
    private AnchorPane profileAnchorPane; // Assuming this is the AnchorPane next to the ListView in AttendanceMain.fxml

    private DatabaseManager dbManager = new DatabaseManager();
    private ObservableList<AttendanceRecordStore> attendanceData = FXCollections.observableArrayList();
    private Map<String, Integer> eventNameToIdMap;
    private int selectedEventId = -1;

    @FXML
    public void initialize() {
        // 1. Populate ComboBox with Event Names
        eventNameToIdMap = dbManager.getEventNamesAndIds();
        ObservableList<String> eventNames = FXCollections.observableArrayList(eventNameToIdMap.keySet());
        eventComboBox.setItems(eventNames);

        // 2. Set up ListView
        attendanceListView.setItems(attendanceData);
        attendanceListView.setCellFactory(listView -> new ListCell<AttendanceRecordStore>() {
            private Parent content;
            private AttendanceCardController controller; // The controller for the FXML template

            @Override
            protected void updateItem(AttendanceRecordStore item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    if (content == null) {
                        try {
                            // Load the FXML template once per cell instance
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("AttendanceCard.fxml"));
                            content = loader.load();
                            controller = loader.getController();
                        } catch (IOException e) {
                            System.err.println("❌ Error loading AttendanceCard.fxml: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    if (controller != null) {
                        // Bind the data from the item to the card's labels
                        controller.setAttendanceRecord(item);
                    }
                    setGraphic(content);
                    // Optional: Remove default cell padding for a cleaner look
                    setStyle("-fx-padding: 0;");
                }
            }
        });

        // 3. Set up event handler for ComboBox selection
        eventComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedEventId = eventNameToIdMap.get(newVal);
                loadAttendanceData(selectedEventId);
            }
        });

        // 4. Set up listener for ListView selection to load profile
        attendanceListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Selected item: " + (newVal != null ? newVal.getFullname() : "None"));
            if (newVal != null) {
                loadProfile(newVal.getAttendanceId());
            } else {
                profileAnchorPane.getChildren().clear();
            }
        });

        // 5. Select the first event by default if any exist
        if (!eventNames.isEmpty()) {
            // ➡️ FIX: Use the selection model to select the first item
            eventComboBox.getSelectionModel().selectFirst();
        }
    }

    private void loadAttendanceData(int eventId) {
        attendanceData.clear();
        if (eventId > 0) {
            attendanceData.addAll(dbManager.getAttendanceByEventId(eventId));
        }
    }

    private void loadProfile(int attendanceId) {
        // Fetch the full attendance record from the database
        AttendanceRecordStore fullRecord = dbManager.getFullAttendanceRecord(attendanceId);
        if (fullRecord == null) {
            System.err.println("No full record found for attendance ID: " + attendanceId);
            return;
        }

        try {
            // Load the AttendanceProfile.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AttendanceProfile.fxml"));
            Parent profile = loader.load();

            // Get the controller and set the data
            AttendanceProfileController controller = loader.getController();
            controller.setAttendanceRecord(fullRecord);

            // Clear the AnchorPane and add the loaded profile
            profileAnchorPane.getChildren().clear();
            profileAnchorPane.getChildren().add(profile);

            // Anchor the profile to fill the pane
            AnchorPane.setTopAnchor(profile, 0.0);
            AnchorPane.setBottomAnchor(profile, 0.0);
            AnchorPane.setLeftAnchor(profile, 0.0);
            AnchorPane.setRightAnchor(profile, 0.0);

        } catch (IOException e) {
            System.err.println("Failed to load AttendanceProfile.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refreshListView() {
        if (selectedEventId > 0) {
            loadAttendanceData(selectedEventId);
        }
    }

    @FXML
    private void handleAddManually() {
        if (selectedEventId <= 0) {
            // TODO: Show an alert if no event is selected
            System.out.println("Please select an event first.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAttendance.fxml"));
            Parent parent = loader.load();

            // Pass necessary data to the popup controller
            AddAttendanceController controller = loader.getController();
            controller.setAttendanceController(this); // Allow popup to call refresh
            controller.setEventId(selectedEventId);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Attendance Manually");
            popupStage.setScene(new Scene(parent));
            popupStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Failed to load AddAttendance.fxml popup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShare() {
        if (selectedEventId <= 0) {
            // TODO: Show an alert
            System.out.println("Please select an event first to share.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AttendanceEventShare.fxml"));
            Parent parent = loader.load();

            AttendanceEventShare controller = loader.getController();
            controller.setEventId(selectedEventId);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Share Event Attendance Link");
            popupStage.setScene(new Scene(parent));
            popupStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Failed to load AttendanceEventShare.fxml popup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}