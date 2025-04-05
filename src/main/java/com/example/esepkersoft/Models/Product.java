package com.example.esepkersoft.Models;

public class Product {
    private int id;
    private String barcode;
    private String name;
    private String type;
    private double price;
    private String unitMeasurement; // "measurable" or "countable"

    // Constructors
    public Product() {}

    public Product(String barcode, String name, String type, double price, String unitMeasurement) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.unitMeasurement = unitMeasurement;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) {
        // Optional validation if you want to restrict to specific types when provided
        if (type != null && !type.isEmpty()) {
            this.type = type.trim();
        } else {
            this.type = null;
        }
    }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getUnitMeasurement() { return unitMeasurement; }
    public void setUnitMeasurement(String unitMeasurement) {
        if (unitMeasurement.equals("measurable") || unitMeasurement.equals("countable")) {
            this.unitMeasurement = unitMeasurement;
        }
    }

    @Override
    public String toString() {
        return String.format("Product [id=%d, barcode=%s, name=%s, price=%.2f]",
                id, barcode, name, price);
    }
}