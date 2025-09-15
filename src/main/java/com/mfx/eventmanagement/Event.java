package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Event extends MainFrame {
    @FXML
    private MFXButton CreateButton;
    @FXML
    private MFXComboBox eventTypeComboBox;

@FXML
    private void actionCreateButton(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EventCreateFix-v2.fxml"));
            Parent root = loader.load();

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


}

