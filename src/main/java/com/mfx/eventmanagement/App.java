package com.mfx.eventmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
            BorderPane root = loader.load();
            MainFrameController controller = loader.getController();

            if (controller != null) {
                System.out.println("✅ MainFrame Controller loaded successfully.");
            } else {
                System.err.println("❌ Controller is null!");
            }

            Scene scene = new Scene(root);
            primaryStage.setTitle("Event Management");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("❌ FXML loading failed!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}