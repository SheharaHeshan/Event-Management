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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import java.io.IOException;

import java.io.IOException;
// This is not the correct way to set margin, but is not needed in this solution
import com.mfx.eventmanagement.EventController;

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
    private AnchorPane scheduleView;

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton CreateButton;

    @FXML
    private MFXButton attendencepass;

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
    public void loadEventMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EventMain.fxml"));
            Parent eventPane = fxmlLoader.load();
            EventController controller = fxmlLoader.getController();

            if (controller != null) {
                System.out.println("MainFrameController: Injecting 'this' into EventController.");
                controller.setMainFrameController(this);
            } else {
                System.err.println("FATAL FXML ERROR: EventController is null after loading EventMain.fxml.");
                return;
            }

            // 4. Set the loaded view (eventPane) to the center of the BorderPane (root)
            // This should now work, as 'root' is initialized in MainFrameController.
            if (root != null) {
                root.setCenter(eventPane);
            } else {
                System.err.println("FATAL UI ERROR: BorderPane 'root' is null in MainFrameController. Check MainFrame.fxml and fx:id=\"root\".");
            }

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

    /**
     * Public method to load the CalendarScheduler view into the center of the MainFrame.
     * @param event The main event data to schedule sub-events under.
     */
    public void loadScheduleView(EventDataStore event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarEventScheduler.fxml"));
            Parent scheduleView = loader.load();

            // Pass the EventDataStore to the new calendar controller
            CalendarEventSchedulerController controller = loader.getController();
            controller.setMainEvent(event);

            root.setCenter(scheduleView); // Load the new view into the BorderPane center
        } catch (IOException e) {
            System.err.println("Failed to load CalendarScheduler.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void loadAttendanceMain() {
        try {
            // Load the FXML for the attendance view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AttendanceMain.fxml"));
            Parent attendanceView = loader.load();

            // Set the attendance view to the center of the BorderPane
            root.setCenter(attendanceView);

            // Optional: The AttendanceController might need to initialize after this
            // You can call an initialization method on the controller if needed.
            // AttendanceController controller = loader.getController();
            // controller.initializeData(); // Example

        } catch (IOException e) {
            System.err.println("❌ FXML Load Failed for AttendanceMain.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


}