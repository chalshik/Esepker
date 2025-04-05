package com.example.esepkersoft.Models;

public class SaleItem {
    private int id;
    private String barcode;
    private String name;
    private double quantity;
    private double price;
    private double totalPrice;
    private int saleId;

    // Constructors
    public SaleItem() {}

    public SaleItem(String barcode, String name, double quantity,
                    double price, int saleId) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
        this.saleId = saleId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
        this.totalPrice = quantity * price;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        this.price = price;
        this.totalPrice = quantity * price;
    }

    public double getTotalPrice() { return totalPrice; }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    @Override
    public String toString() {
        return String.format("SaleItem [product=%s, qty=%.2f, price=%.2f]",
                name, quantity, price);
    }
}