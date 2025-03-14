package com.example.esepkersoft.Models;

public class Sales {
    private String barcode;
    private String payment;
    private double income;
    private double quantity;

    public Sales(String barcode, String payment, double income, double quantity) {
        this.barcode = barcode;
        this.payment = payment;
        this.income = income;
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getPayment() {
        return payment;
    }

    public double getIncome() {
        return income;
    }

    public double getQuantity() {
        return quantity;
    }
}
