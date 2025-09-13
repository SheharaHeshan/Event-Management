package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFrame implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private VBox sidebarBorderPane;
    @FXML
    private HBox hboxpane;
    @FXML
    private MFXToggleButton toggleSidebarButton;

    //toggle state
    private boolean isExpanded = true;
    private static final double EXPANDED_WIDTH = 200.0;
    private static final double COLLAPSED_WIDTH = 50.0;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Dashboard Controller initialized.");
    }
}