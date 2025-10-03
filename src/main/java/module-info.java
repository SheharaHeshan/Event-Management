module com.emgmt.eventmgmt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;

    opens com.emgmt.eventmgmt to javafx.fxml;
    exports com.emgmt.eventmgmt;
}