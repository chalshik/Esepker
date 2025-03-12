package com.example.esepkersoft.Controllers;

import com.example.esepkersoft.Services.ScannerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.example.esepkersoft.Services.ProductOperations;

import java.util.List;
import java.util.Map;

public class ScannerController {
    @FXML
    private TextField barcodeInput;

    @FXML
    private ListView<String> cartListView;
    private final ScannerService barcodeScannerService = new ScannerService();

    @FXML
    public void initialize() {
        // Make barcode input hidden
        barcodeInput.setOpacity(0);
        barcodeInput.setFocusTraversable(false);

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

    private void handleBarcode(String barcode) {
        System.out.println("Handling barcode: " + barcode);

        // Add scanned barcode to shopping cart
        if (cartListView != null) {
            List<Map<String,Object>> data= ProductOperations.getProductByBarcode(barcode);
            for(Map<String,Object> dat:data){
                cartListView.getItems().add("Product: " + dat.get("name"));
                System.out.println("Barcode added to cart: " + barcode);
            }

        } else {
            System.out.println("cartListView is null!");
        }
    }
}
