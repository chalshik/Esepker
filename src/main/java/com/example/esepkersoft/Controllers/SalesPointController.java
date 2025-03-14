package com.example.esepkersoft.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import com.example.esepkersoft.Services.ScannerService;

public class SalesPointController {
    @FXML
    private Group cashOptionGroup;
    @FXML
    private Circle cashCircle;
    @FXML
    private Circle cardCircle;
    @FXML
    private TextField barcodeInput;
    @FXML
    private ListView<String> productsList;



    // Sale Point (Product ComboBox and Price Label)
    @FXML
    private ComboBox<String> unitComboBox;
    @FXML
    private Label clientChange;
    
    private final ScannerService barcodeScannerService = new ScannerService();

    @FXML
    public void initialize() {
        // Set cashCircle to blue by default
        cashCircle.setFill(Color.BLUE);

        // Set ComboBox items for products
//        unitComboBox.getItems().addAll("гр", "кг");
//        unitComboBox.setValue("гр"); // Default selection

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
}
