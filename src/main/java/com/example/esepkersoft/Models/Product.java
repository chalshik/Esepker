package com.example.esepkersoft.Models;

import javafx.beans.property.SimpleStringProperty;

public class Product {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty unit;
    private final SimpleStringProperty price;

    public Product(String barcode, String amount, String unit, String price) {
        this.barcode = new SimpleStringProperty(barcode);
        this.amount = new SimpleStringProperty(amount);
        this.unit = new SimpleStringProperty(unit);
        this.price = new SimpleStringProperty(price);
    }

    // Getters
    public String getBarcode() {
        return barcode.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getUnit() {
        return unit.get();
    }

    public String getPrice() {
        return price.get();
    }

    // Property getters for TableView binding
    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }
}

