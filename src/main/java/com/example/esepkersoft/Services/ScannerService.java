package com.example.esepkersoft.Services;

import javafx.scene.control.TextField;
import java.util.function.Consumer;
public class ScannerService {
    private Consumer<String> barcodeListener;

    public void setOnBarcodeScanned(Consumer<String> listener) {
        this.barcodeListener = listener;
    }

    public void handleBarcodeInput(String barcode) {
        if (barcodeListener != null && barcode != null && !barcode.isEmpty()) {
            barcodeListener.accept(barcode.trim());
        }
    }
}
