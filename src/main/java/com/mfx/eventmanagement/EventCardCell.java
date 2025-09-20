package com.mfx.eventmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import java.io.IOException;


public class EventCardCell extends ListCell<Event> {

    private VBox eventCard;
    private EventCardController cardController;

    public EventCardCell() {
        // Load the FXML and get the controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EventCard.fxml"));
        try {
            eventCard = fxmlLoader.load();
            cardController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Event event, boolean empty) {
        super.updateItem(event, empty);

        if (empty || event == null) {
            setGraphic(null);
        } else {
            // Populate the card with data from the Event object
            cardController.setEventData(
                    event.name(),
                    event.description(),
                    event.startDate(),
                    event.endDate(),
                    event.eventType(),
                    true // You can determine this status based on other logic, e.g., dates
            );
            setGraphic(eventCard);
        }
    }
}