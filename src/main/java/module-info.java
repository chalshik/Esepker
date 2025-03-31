module com.example.esepkersoft {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.sql;
    requires jdk.incubator.vector;

    opens com.example.esepkersoft to javafx.fxml;
    exports com.example.esepkersoft;

    exports com.example.esepkersoft.Controllers;
    opens com.example.esepkersoft.Controllers to javafx.fxml;

    // ✅ Allow JavaFX to access properties in Sales class
    opens com.example.esepkersoft.Models to javafx.base;
}
