package com.example.esepkersoft.Controllers;

import com.example.esepkersoft.Services.ScannerService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

        // Set up barcode scanner functionality
        barcodeScannerService.setOnBarcodeScanned(this::handleBarcode);

        // Detect barcode input when "Enter" is pressed
        barcodeInput.setOnAction(event -> {
            String scannedCode = barcodeInput.getText();
            barcodeScannerService.handleBarcodeInput(scannedCode);
            barcodeInput.clear();  // Clear after processing
        });
    }

    private void handleBarcode(String barcode) {
        System.out.println("Scanned: " + barcode);

        // Add scanned barcode to shopping cart
        if (cartListView != null) {
            cartListView.getItems().add("Product: " + barcode);
        }
    }
}
