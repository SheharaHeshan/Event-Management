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
    requires MaterialFX;
    requires org.jetbrains.annotations;
    requires com.calendarfx.view;

    opens com.mfx.eventmanagement to javafx.fxml;
    exports com.mfx.eventmanagement;
}