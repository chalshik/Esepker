package com.example.esepkersoft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.esepkersoft.Services.dbManager;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Views/SalesPoint.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400); // Larger window size
        stage.setTitle("Barcode Scanner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
//        dbManager db = dbManager.getInstance();
//        db.executeSet("INSERT INTO products (barcode, name) VALUES('4690302738365', 'tomat')");
        launch();
    }
}