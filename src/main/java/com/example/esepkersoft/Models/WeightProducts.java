package com.example.esepkersoft.Models;

public class WeightProducts {
    private String barcode;
    private String name;
    private double purchase_price;
    private double sale_price;
    private double quantity;

    public WeightProducts(String barcode, String name, double purchase_price, double sale_price, double quantity) {
        this.barcode = barcode;
        this.name = name;
        this.purchase_price = purchase_price;
        this.sale_price = sale_price;
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public double getSale_price() {
        return sale_price;
    }

    public double getQuantity() {
        return quantity;
    }
}