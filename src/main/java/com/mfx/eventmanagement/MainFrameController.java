package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox; // Import HBox to use it for layout
import javafx.geometry.Pos; // Import Pos for alignment
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
// This is not the correct way to set margin, but is not needed in this solution

public class MainFrameController {
    @FXML
    private MFXButton toggleButton;
    @FXML
    private VBox toggleBar;
    @FXML
    private MFXButton EventPass;

    @FXML
    private AnchorPane eventPane;

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton CreateButton;

    private boolean isExpanded = true;

    private static final double EXPANDED_WIDTH = 200;
    private static final double COLLAPSED_WIDTH = 80.0;
    private static final double EXPANDED_MARGIN = 145.0;
    private static final double COLLAPSED_MARGIN = 20.0;


    @FXML
    private void toggleAction() {
        isExpanded = !isExpanded;

        toggleBar.setPrefWidth(isExpanded ? EXPANDED_WIDTH : COLLAPSED_WIDTH);
        VBox.setMargin(toggleButton, new Insets(10.0, 0, 0, isExpanded ? EXPANDED_MARGIN : COLLAPSED_MARGIN));

    }

    @FXML
    private void loadEventMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EventMain.fxml"));
            AnchorPane eventPane = loader.load();
//            mainContent.getChildren().setAll(eventPane);

            root.setCenter(eventPane);

            // Set anchors to make the loaded pane fill the container
            AnchorPane.setTopAnchor(eventPane, 0.0);
            AnchorPane.setBottomAnchor(eventPane, 0.0);
            AnchorPane.setLeftAnchor(eventPane, 0.0);
            AnchorPane.setRightAnchor(eventPane, 0.0);

        } catch (IOException e) {
            // New: Print a visible message to the console AND show an Alert in the UI (recommended for production)
            System.err.println("❌ FXML Load Failed for EventMain.fxml: " + e.getMessage());
            e.printStackTrace(); // Keep this for detailed stack trace
            // You can also add a JavaFX Alert here to notify the user/tester
        }
    }

//    @FXML
//    private void actionCreateButton(ActionEvent event) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EventCreate.fxml"));
//            Stage stage = new Stage();
//            stage.setScene(new Scene(fxmlLoader.load()));
//
//            // Get the controller of the newly created pop-up
//            EventCreateController eventCreateController = fxmlLoader.getController();
//
//            // Crucial step: Pass a reference to THIS MainFrame instance to the pop-up controller
//            eventCreateController.setMainFrameController(this);
//
//            stage.setTitle("Create New Event");
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}