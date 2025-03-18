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
        dbManager  db  = dbManager.getInstance();
        stage.setMaximized(true);
        
        stage.show();
    }

    public static void main(String[] args) {
        dbManager  db  = dbManager.getInstance();

        String insertProductsQuery = "INSERT INTO products (barcode, name, type, purchase_price, sale_price) VALUES "
                + "('1234567890', 'Samsung Galaxy S21', 'Electronics', 699.00, 799.00), "
                + "('2345678901', 'Apple iPhone 13', 'Electronics', 799.00, 999.00), "
                + "('3456789012', 'Levis 501 Jeans', 'Clothing', 45.00, 65.00), "
                + "('4567890123', 'Nike Air Max 270', 'Clothing', 80.00, 120.00), "
                + "('5678901234', 'Organic Avocados', 'Groceries', 1.50, 2.50), "
                + "('6789012345', 'Bananas', 'Groceries', 0.75, 1.00), "
                + "('7890123456', 'IKEA Hemnes Dresser', 'Furniture', 200.00, 250.00), "
                + "('8901234567', 'Serta Perfect Sleeper Mattress', 'Furniture', 500.00, 600.00), "
                + "('9012345678', 'LEGO Star Wars Millennium Falcon', 'Toys', 150.00, 200.00), "
                + "('0123456789', 'Barbie Dream House', 'Toys', 120.00, 150.00);";

        db.executeSet(insertProductsQuery);

        launch();
    }
}