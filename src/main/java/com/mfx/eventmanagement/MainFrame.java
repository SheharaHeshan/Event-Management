package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.SelectionMode;

import javafx.scene.control.ListView; // Standard JavaFX ListView


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFrame implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private VBox sidebarVBox;
    @FXML
    private HBox hboxpane;
    @FXML
    private MFXToggleButton toggleSidebarButton;
    @FXML
    private MFXButton CreateButton;
    @FXML
    private MFXButton PassShedule;
     @FXML
    private ListView<Event> eventListView;

    // buttons in sidebar
    @FXML
    private MFXButton dashboardButton;
    @FXML
    private MFXButton eventButton;
    @FXML
    private MFXButton scheduleButton;
    @FXML
    private MFXButton attendanceButton;
    @FXML
    private MFXButton teamButton;

    // toggle state
    private boolean isExpanded = true;
    private static final double EXPANDED_WIDTH = 200.0;
    private static final double COLLAPSED_WIDTH = 50.0;

    /**
     * Initializes the controller. This method is called after the FXML has been loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MainFrame Controller initialized.");

        // Set the custom cell factory for the ListView
        eventListView.setCellFactory(param -> new EventCardCell());

        // Bind the ListView's items to the ObservableList
        eventListView.setItems(events);

        // Add a listener to handle selection
        eventListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        eventListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected Event: " + newValue.name());
                // Here, you can get the selected item and update the UI accordingly.
                // For example, you can enable an "Edit" button.
            }
        });


    }

    // Use an ObservableList to hold event data
    private final ObservableList<Event> events = FXCollections.observableArrayList();



    // This method is called from the EventCreate controller
    public void addEventToList(Event newEvent) {
        // Add the new Event object to the ObservableList
        events.add(0, newEvent); // Add to the top of the list
    }



    /**
     * Toggles the visibility and width of the sidebar.
     */
    @FXML
    public void toggleSidebar() {
        if (isExpanded) {
            collapseSidebar();
        } else {
            expandSidebar();
        }
        isExpanded = !isExpanded;
    }

    // action for CreateEvent Button
    @FXML
    private void actionCreateButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EventCreateFix-v2.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));

            // Get the controller of the newly created pop-up
            EventCreate eventCreateController = fxmlLoader.getController();

            // Crucial step: Pass a reference to THIS MainFrame instance to the pop-up controller
            eventCreateController.setMainFrameController(this);

            stage.setTitle("Create New Event");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // New action method for the "Schedule Event" button
    @FXML
    private void actionPassShedule(ActionEvent event) {
        System.out.println("Switching to Calendar scene...");

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Note: CalendarApp must exist for this to work
            Application calendarApp = new CalendarApp();
            calendarApp.start(currentStage);

            System.out.println("Scene switched successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Collapses the sidebar to a predefined width.
     */
    private void collapseSidebar() {
        sidebarVBox.setPrefWidth(COLLAPSED_WIDTH);
        dashboardButton.setText("");
        eventButton.setText("");
        scheduleButton.setText("");
        attendanceButton.setText("");
        teamButton.setText("");

        dashboardButton.setAlignment(javafx.geometry.Pos.CENTER);
        eventButton.setAlignment(javafx.geometry.Pos.CENTER);
        scheduleButton.setAlignment(javafx.geometry.Pos.CENTER);
        attendanceButton.setAlignment(javafx.geometry.Pos.CENTER);
        teamButton.setAlignment(javafx.geometry.Pos.CENTER);
    }

    /**
     * Expands the sidebar to its original width.
     */
    private void expandSidebar() {
        sidebarVBox.setPrefWidth(EXPANDED_WIDTH);
        dashboardButton.setText("Dashboard");
        eventButton.setText("Events");
        scheduleButton.setText("Schedule");
        attendanceButton.setText("Attendance");
        teamButton.setText("Team");

        dashboardButton.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        eventButton.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        scheduleButton.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        attendanceButton.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        teamButton.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    }

    // functions for click user buttons
    @FXML
    private void handleDashboardAction(ActionEvent event) {
        System.out.println("Event button clicked!");
    }

    @FXML
    private void handleEventAction(ActionEvent event) {
        System.out.println("Event button clicked!");
    }

    @FXML
    private void handleScheduleAction(ActionEvent event) {
        System.out.println("Schedule button clicked!");
    }

    @FXML
    private void handleAttendanceAction(ActionEvent event) {
        System.out.println("Attendance button clicked!");
    }

    @FXML
    private void handleTeamAction(ActionEvent event) {
        System.out.println("Team button clicked!");
    }
}