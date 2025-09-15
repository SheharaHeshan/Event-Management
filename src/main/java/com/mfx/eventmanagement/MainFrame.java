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
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class MainFrame implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private VBox sidebarBorderPane;
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

    // Use an ObservableList to hold the events
    private ObservableList<Event> events = FXCollections.observableArrayList();

    //toggle state
    private boolean isExpanded = true;
    private static final double EXPANDED_WIDTH = 200.0;
    private static final double COLLAPSED_WIDTH = 50.0;

    /**
     * Data class to represent a single event.
     * This record simplifies data handling.
     */
    public record Event(String name, String type, LocalDate startDate, LocalDate endDate) {}


    /**
     * Initializes the controller. This method is called after the FXML has been loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add initial sample events to the already initialized list
        events.addAll(
                new Event("Project Meeting", "Work", LocalDate.of(2025, 1, 15), LocalDate.of(2025, 1, 15)),
                new Event("Team Lunch", "Social", LocalDate.of(2025, 1, 20), LocalDate.of(2025, 1, 20)),
                new Event("Client Workshop", "Training", LocalDate.of(2025, 2, 5), LocalDate.of(2025, 2, 7))
        );
        eventListView.setItems(events);

        // Set a custom cell factory to load the EventCard.fxml for each item
        eventListView.setCellFactory(param -> new ListCell<>() {
            private VBox eventCard;
            private EventCardController controller;

            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);

                if (empty || event == null) {
                    setGraphic(null);
                } else {
                    if (eventCard == null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("EventCard.fxml"));
                            eventCard = loader.load();
                            controller = loader.getController();
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle the error gracefully
                        }
                    }
                    if (controller != null) {
                        controller.setEventData(event);
                    }
                    setGraphic(eventCard);
                }
            }
        });
        System.out.println("Dashboard Controller initialized.");
    }

    /**
     * Adds a new event to the ListView's observable list.
     * This method is called by the EventCreate controller.
     * @param newEvent The new Event object to add.
     */
    public void addEventToList(Event newEvent) {
        if (newEvent != null) {
            events.add(newEvent);
        }
    }

    /**
     * Toggles the visibility and width of the sidebar.
     * This method is called by the MFXToggleButton's onAction event.
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
    private void actionCreateButton(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EventCreateFix-v2.fxml"));
            Parent root = loader.load();

            // Get the controller for the popup and set a reference to this MainFrame
            EventCreate popupController = loader.getController();
            popupController.setMainFrameController(this);

            Stage popupStage = new Stage();
            // Set the new stage to be a popup/dialog window
            popupStage.initModality(Modality.APPLICATION_MODAL);
            // Set the owner of the popup to be the main window
            popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            // Set the scene for the popup
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

        // Get the current stage from the button's ActionEvent
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Create a new instance of CalendarApp
            Application calendarApp = new CalendarApp();

            // Call the start method to set the new scene on the existing stage
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
        sidebarBorderPane.setPrefWidth(COLLAPSED_WIDTH);
    }

    /**
     * Expands the sidebar to its original width.
     */
    private void expandSidebar() {
        sidebarBorderPane.setPrefWidth(EXPANDED_WIDTH);
    }
}
