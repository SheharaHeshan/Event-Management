package com.mfx.eventmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
        Scene scene = new Scene(loader.load(), 1024, 768);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/mfx/eventmanagement/styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Event Scheduler");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}