module com.mfx.eventmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires org.jetbrains.annotations;
    requires com.calendarfx.view;
    requires javafx.base;
    requires javafx.graphics;
    requires MaterialFX;
    requires jakarta.mail;

    requires com.dlsc.gemsfx;
    requires java.sql;
    requires jfxtras.controls;
    requires java.desktop;
    //requires com.mfx.eventmanagement;

    opens com.mfx.eventmanagement to javafx.fxml;
    exports com.mfx.eventmanagement;



}