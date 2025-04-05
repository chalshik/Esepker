package com.example.esepkersoft.Models;

import java.time.LocalDateTime;

public class Sale {
    private int id;
    private LocalDateTime date;
    private String paymentMethod;
    private double total;

    // Constructors
    public Sale() {}

    public Sale(String paymentMethod, double total) {
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.date = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    @Override
    public String toString() {
        return String.format("Sale [id=%d, date=%s, total=%.2f]",
                id, date, total);
    }
}