package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
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
    // @FXML
    // private ListView<Event> eventListView; // Removed

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EventCreateFix-v2.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.setTitle("Create Event");
            popupStage.showAndWait();

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