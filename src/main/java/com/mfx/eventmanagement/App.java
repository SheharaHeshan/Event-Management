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
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;



            // Load the FXML file

            FXMLLoader loader = createFXMLLoader();

            // Load the root pane
            BorderPane root = loader.load();


            // Get the controller instance
            MainFrame controller = loader.getController();
            if (controller != null) {
                System.out.println(" Controller loaded successfully");
            } else {
                System.err.println(" Controller is null!");
            }

            // Create and configure the scene
            Scene scene = createScene(root);

            // Configure the primary stage
            configurePrimaryStage(primaryStage, scene);

            // Show the application
            primaryStage.show();




    }

    /**
     * Create and configure the FXML loader
     */
    private FXMLLoader createFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();

        // Set the location of your FXML file
        // Make sure this path matches your actual FXML file location
        String fxmlPath = "/com/mfx/eventmanagement/MainFrame.fxml";
        loader.setLocation(getClass().getResource(fxmlPath));


        return loader;
    }


    private Scene createScene(BorderPane root) {


        Scene scene = new Scene(root);

        return scene;
    }


    private void configurePrimaryStage(Stage primaryStage, Scene scene) {
        System.out.println(" Configuring main window...");


        primaryStage.setTitle("Event Management Dashboard - Toggle Sidebar Test");

        // Set the scene
        primaryStage.setScene(scene);

        // Set minimum window size
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Set initial window size
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);


        primaryStage.centerOnScreen();


        primaryStage.setOnCloseRequest(event -> {
            System.out.println(" Application closing...");
            handleApplicationClose();
        });

        System.out.println(" Main window configured");
    }


    private void handleApplicationClose() {

        System.out.println("Cleaning up resources...");

        System.out.println(" Application closed successfully");
    }



    @Override
    public void stop() {
        System.out.println(" Application stop() method called");
        handleApplicationClose();
    }


    public static void main(String[] args) {

        launch(args);

        System.out.println(" Main method completed");
    }




}

