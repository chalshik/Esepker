module com.example.esepkersoft {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.esepkersoft to javafx.fxml;
    exports com.example.esepkersoft;
    exports com.example.esepkersoft.Controllers;
    opens com.example.esepkersoft.Controllers to javafx.fxml;
}
