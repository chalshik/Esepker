package com.example.esepkersoft.Controllers;

import com.example.esepkersoft.Models.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import com.example.esepkersoft.Services.ScannerService;
import javafx.scene.control.TextField;

public class SalesPointController {


    @FXML
    private TableView<Product> productsTableView;
    // Reference the columns
    @FXML
    private TableColumn<Product, String> productColumn;
    @FXML
    private TableColumn<Product, String> amountColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;


    @FXML
    private TextField barcodeInput;
    @FXML
    private TextField amountInput;
    @FXML
    private ComboBox<String> unitComboBox;
    @FXML
    private Label pieceLabel; // to replace unitComboBox with ШТ

    @FXML
    private Label totalPriceLabel;
    @FXML
    private Group paymentOptionGroup;
    @FXML
    private Circle cashCircle;
    @FXML
    private Circle cardCircle;

    @FXML
    private Group cashOptionGroup;
    @FXML
    private TextField clientMoney; // how much client gave
    @FXML
    private Label clientChange; // СДАЧА


    @FXML
    private Button finishButton;






    
    private final ScannerService barcodeScannerService = new ScannerService();

    @FXML
    public void initialize() {
        // Set cashCircle to blue by default
        cashCircle.setFill(Color.BLUE);

        // Set ComboBox items for products
        unitComboBox.getItems().addAll("гр", "кг");
        unitComboBox.setValue("гр"); // Default selection

        // Configure the existing columns from FXML
        productColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        
        amountColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            return new SimpleStringProperty(product.getAmount() + " " + product.getUnit());
        });
        
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        // Initialize items list
        productsTableView.setItems(FXCollections.observableArrayList());
        
        // Load dummy data
        loadDummyData();
        amountInput.setOnAction(event -> {
            String barcode = barcodeInput.getText();
            String amount = amountInput.getText();
            String unit = unitComboBox.getValue();
            String price = "0"; // Placeholder for the price. Set it according to your logic.
        
            // Check if all inputs are filled
            if (barcode != null && !barcode.isEmpty() && amount != null && !amount.isEmpty() && unit != null) {
                // Create a new Product with the input data
                Product newProduct = new Product(barcode, amount, unit, price);
        
                // Add the new product to the TableView's items
                productsTableView.getItems().add(newProduct);
        
                // Optionally, clear inputs after adding the product
                barcodeInput.clear();
                amountInput.clear();
                unitComboBox.setValue("гр"); // Reset ComboBox to default
            }
        });
        Platform.runLater(() -> {
            Node header = productsTableView.lookup(".column-header-background");
            if (header != null) {
                header.setStyle("-fx-max-height: 0; -fx-pref-height: 0; -fx-min-height: 0;");
            }
        });




        // Request focus on the barcode input field
        Platform.runLater(() -> barcodeInput.requestFocus());

        // Set up barcode scanner functionality
        barcodeScannerService.setOnBarcodeScanned(this::handleBarcode);

        // Detect barcode input when "Enter" is pressed
        barcodeInput.setOnAction(event -> {
            String scannedCode = barcodeInput.getText();
            if (scannedCode != null && !scannedCode.isEmpty()) {
                barcodeScannerService.handleBarcodeInput(scannedCode);
                barcodeInput.clear();  // Clear after processing
            }
        });

}

    // Circles functionality
    @FXML
    private void handleCashClick() {
        cashOptionGroup.setVisible(true);

        cashCircle.setFill(Color.BLUE); // Set cashCircle to blue
        cardCircle.setFill(Color.WHITE); // Reset cardCircle to default color
    }

    @FXML
    private void handleCardClick() {
        cashOptionGroup.setVisible(false);

        cardCircle.setFill(Color.BLUE); // Set cardCircle to blue
        cashCircle.setFill(Color.WHITE); // Reset cashCircle to default color
        
    }
    
    private void handleBarcode(String barcode) {
        System.out.println("Handling barcode: " + barcode);
        barcodeInput.setText(barcode);
    }
    
    // Add this method to your SalesPointController class
    private void loadDummyData() {
        // Clear existing items
        productsTableView.getItems().clear();
        
        // Add weight-based products (kg)
        addDummyProduct("1234567890123", "Яблоки", "кг", "150");
        addDummyProduct("2345678901234", "Картофель", "кг", "80");
        
        // Add weight-based products (gram)
        addDummyProduct("3456789012345", "Орехи", "гр", "1200");
        addDummyProduct("4567890123456", "Конфеты", "гр", "800");
        
        // Add piece-based products
        addDummyProduct("5678901234567", "Хлеб", "шт", "45");
        addDummyProduct("6789012345678", "Молоко", "шт", "95");
        
        // Update total price
        updateTotalPrice();
    }

    private void addDummyProduct(String barcode, String name, String unit, String pricePerUnit) {
        // Set default amount based on unit type
        String amount;
        if (unit.equals("гр")) {
            amount = "100";
        } else if (unit.equals("кг")) {
            amount = "1";
        } else { // piece
            amount = "1";
        }
        
        // Calculate price based on amount and price per unit
        double priceValue;
        if (unit.equals("гр")) {
            // Convert price per kg to price for the given grams
            priceValue = Double.parseDouble(pricePerUnit) * Double.parseDouble(amount) / 1000;
        } else {
            priceValue = Double.parseDouble(pricePerUnit) * Double.parseDouble(amount);
        }
        
        String price = String.format("%.2f", priceValue);
        
        // Create and add the product
//        Product product = new Product(name, amount, unit, price);
//        product.setBarcode(barcode);
//        productsTableView.getItems().add(product);
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Product product : productsTableView.getItems()) {
            total += Double.parseDouble(product.getPrice());
        }
        totalPriceLabel.setText(String.format("%.2f", total));
    }
}
