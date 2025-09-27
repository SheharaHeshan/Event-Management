package com.mfx.eventmanagement;

import com.mfx.eventmanagement.EventCreateController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox; // Import HBox to use it for layout
import javafx.geometry.Pos; // Import Pos for alignment
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane; // EventCard is an AnchorPane
import java.io.IOException;
import java.util.List;

public class EventController extends MainFrameController {

    @FXML
    private MFXButton CreateButton;
    @FXML
    private MFXListView<AnchorPane> eventList; // Use AnchorPane as the list item type

    private DatabaseManager dbManager = new DatabaseManager(); // Initialize DB Manager

    /**
     * Initializes the controller. This runs when the FXML is loaded.
     */
    @FXML
    public void initialize() {
        //   Load all events when the application starts
        loadInitialEvents();
    }

    /**
     * Loads all saved events from the database and displays them in the list view.
     */
    public void loadInitialEvents() {
        // Clear any existing items (useful if calling this to refresh)
        eventList.getItems().clear();

        // Load data from the database
        List<EventDataStore> savedEvents = dbManager.loadAllEvents();

        // Iterate through the list and create a card for each event
        for (EventDataStore event : savedEvents) {
            loadEventCardToListView(event);
        }
    }



    /**
     * Dynamically loads EventCard.fxml, populates its labels with the event data,
     * and adds it to the MFXListView.
     * @param event The Event object to display.
     */
    public void loadEventCardToListView(EventDataStore event) {
        try {
            // 1. Load the FXML for the card
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EventCard.fxml"));
            AnchorPane eventCard = fxmlLoader.load();

            // 2. Get the controller for the card
            EventCardController cardController = fxmlLoader.getController();

            // 3. Set the data on the card's labels
            cardController.setEventData(event);

            // 4. Add the configured card to the MFXListView
            eventList.getItems().add(0, eventCard); // Add to the top of the list

        } catch (IOException e) {
            System.err.println("Failed to load EventCard.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void actionCreateButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EventCreate.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));

            // Get the controller of the newly created pop-up
            EventCreateController eventCreateController = fxmlLoader.getController();

            // Crucial step: Pass a reference to THIS MainFrame instance to the pop-up controller
            eventCreateController.setMainFrameController(this);

            stage.setTitle("Create New Event");
            stage.initModality(Modality.APPLICATION_MODAL);

            // --- NEW: Wait for the popup to close ---
            stage.showAndWait();

            // --- NEW: Check for and load the new event ---
            EventDataStore newEvent = eventCreateController.getCreatedEvent();
            if (newEvent != null) {
                loadEventCardToListView(newEvent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

