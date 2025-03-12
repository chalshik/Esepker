package com.example.esepkersoft.Services;

import java.util.function.Consumer;

public class ScannerService {
    private Consumer<String> barcodeListener;

    public void setOnBarcodeScanned(Consumer<String> listener) {
        this.barcodeListener = listener;
        System.out.println("Barcode listener set successfully.");
    }
    public void handleBarcodeInput(String barcode) {
        System.out.println("Received barcode input: " + barcode);
        if (barcodeListener != null && barcode != null && !barcode.isEmpty()) {
            barcodeListener.accept(barcode.trim());
            System.out.println("Barcode processed: " + barcode);
        } else {
            System.out.println("Barcode input was empty or listener was null.");
        }
    }
}