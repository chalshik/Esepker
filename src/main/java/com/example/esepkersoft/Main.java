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
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Barcode Scanner");
        stage.setScene(scene);
        
        // Make application full screen
        stage.setMaximized(true);
        stage.setFullScreen(true);
        
        stage.show();
    }

    public static void main(String[] args) {

        launch();
   }
}