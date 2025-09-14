package com.mfx.eventmanagement;

import com.calendarfx.view.TimeField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the EventCreate.fxml file.
 * This class handles all the user interactions and logic for the event creation form.
 */
public class EventCreate implements Initializable {

    // FXML components with their fx:id

    @FXML
    private MFXComboBox<String> eventTypeComboBox;
    @FXML
    private MFXDatePicker startDatePicker;
    @FXML
    private MFXDatePicker endDatePicker;
    @FXML
    private MFXTextField EventName;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private MFXButton eventCreatebtn;

    private MainFrame mainFrameController;
    /**
     * Sets the reference to the main controller. This method is called by MainFrame.
     * @param mainFrame The MainFrame controller instance.
     */
    public void setMainFrameController(MainFrame mainFrame) {
        this.mainFrameController = mainFrame;
    }



    // The initialize method is called after all FXML components have been injected.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the event type combo box with options.
        // The event types "In-Person (Physical)", "Virtual (Online)", and "Hybrid" are added here.
        ObservableList<String> eventTypes = FXCollections.observableArrayList(
                "In-Person (Physical)", "Virtual (Online)", "Hybrid"
        );
        eventTypeComboBox.setItems(eventTypes);
    }
}
