package com.mfx.eventmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            // Corrected to load the FXML file that contains the new button
            FXMLLoader loader = createFXMLLoader("Eventv2.fxml");

            // Load the root pane
            BorderPane root = loader.load();

            // Get the controller instance
            MainFrame controller = loader.getController();
            if (controller != null) {
                System.out.println("✅ MainFrame Controller loaded successfully.");
            } else {
                System.err.println("❌ Controller is null!");
            }

            // Create and configure the scene
            Scene scene = createScene(root);

            // Configure the primary stage
            configurePrimaryStage(primaryStage, scene);

            // Show the application
            primaryStage.show();

        } catch (IOException e) {
            handleFXMLLoadError(e);
        } catch (Exception e) {
            handleUnexpectedError(e);
        }
    }

    /**
     * Create and configure the FXML loader
     */
    private FXMLLoader createFXMLLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        return loader;
    }

    private Scene createScene(BorderPane root) {
        return new Scene(root);
    }

    private void configurePrimaryStage(Stage primaryStage, Scene scene) {
        System.out.println("✅ Configuring main window...");
        primaryStage.setTitle("Event Management");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Application closing...");
            handleApplicationClose();
        });
    }

    private void handleFXMLLoadError(IOException e) {
        System.err.println("❌ ========================================");
        System.err.println("❌ FXML loading failed!");
        e.printStackTrace();
        System.err.println("❌ ========================================");
    }

    private void handleUnexpectedError(Exception e) {
        System.err.println("❌ ========================================");
        System.err.println("❌ An unexpected error occurred: " + e.getMessage());
        e.printStackTrace();
        System.err.println("❌ ========================================");
    }

    private void handleApplicationClose() {
        System.out.println("🧹 Cleaning up resources...");
        System.out.println("✅ Application closed successfully.");
    }

    @Override
    public void stop() {
        System.out.println("🛑 Application stop() method called.");
        handleApplicationClose();
    }

    public static void main(String[] args) {
        System.out.println("🎬 ========================================");
        System.out.println("🎬 LAUNCHING JAVAFX APPLICATION");
        System.out.println("🎬 ========================================");
        launch(args);
        System.out.println("👋 Main method completed");
    }
}

