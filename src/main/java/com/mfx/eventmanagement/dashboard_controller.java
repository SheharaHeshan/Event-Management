package com.mfx.eventmanagement;

import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class dashboard_controller implements Initializable {

    @FXML private BorderPane sidebarBorderPane;
    @FXML private MFXToggleButton toggleSidebarButton;
    @FXML private Pane mainContentPane;

    private boolean isExpanded = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize sidebar as expanded
        sidebarBorderPane.getStyleClass().add("sidebar-expanded");

        // Toggle action: Collapse/Expand sidebar
        toggleSidebarButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) { // Expand (selected)
                sidebarBorderPane.getStyleClass().remove("sidebar-collapsed");
                sidebarBorderPane.getStyleClass().add("sidebar-expanded");
                toggleSidebarButton.getStyleClass().remove("toggle-button-center");
                toggleSidebarButton.getStyleClass().add("toggle-button-right");
                toggleSidebarButton.setGraphic(createImageView("/com/mfx/eventmanagement/icons/hamburger.png"));
                isExpanded = true;
            } else { // Collapse (unselected)
                sidebarBorderPane.getStyleClass().remove("sidebar-expanded");
                sidebarBorderPane.getStyleClass().add("sidebar-collapsed");
                toggleSidebarButton.getStyleClass().remove("toggle-button-right");
                toggleSidebarButton.getStyleClass().add("toggle-button-center");
                toggleSidebarButton.setGraphic(createImageView("/com/mfx/eventmanagement/icons/expand.png"));
                isExpanded = false;
            }
            // Adjust main content width
            mainContentPane.prefWidthProperty().unbind();
            mainContentPane.prefWidthProperty().bind(
                    sidebarBorderPane.getScene().getWindow().widthProperty()
                            .subtract(isExpanded ? 200 : 50)
            );
        });
    }

    private ImageView createImageView(String imagePath) {
        ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        iv.setFitWidth(24);
        iv.setFitHeight(24);
        iv.setPreserveRatio(true);
        return iv;
    }
}