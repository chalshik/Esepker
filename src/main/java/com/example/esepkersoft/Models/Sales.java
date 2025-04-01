package com.example.esepkersoft.Models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
public class Sales {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty name;
    private SimpleObjectProperty<Double> amount;
    private final SimpleDoubleProperty priceForPiece;
    private SimpleDoubleProperty totalPriceOfProduct;

    public Sales(String barcode, String name, Double amount, Double priceForPiece) {
        this.barcode = new SimpleStringProperty(barcode);
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleObjectProperty<Double>(amount);
        this.priceForPiece = new SimpleDoubleProperty(priceForPiece);
        this.totalPriceOfProduct = new SimpleDoubleProperty(amount * priceForPiece);

    }

    public Double getAmount() {
        return amount.get();
    }
    public void setAmount(Double newAmount) {
        this.amount.set(round(newAmount));
        this.totalPriceOfProduct.set(round(newAmount * priceForPiece.get()));
    }

    public SimpleObjectProperty<Double> amountProperty() {
        return amount;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public Double getPriceForPiece() {
        return priceForPiece.get();
    }

    public SimpleDoubleProperty priceForPieceProperty() {
        return priceForPiece;
    }

    public double getTotalPriceOfProduct() {
        return totalPriceOfProduct.get();
    }

    public SimpleDoubleProperty totalPriceOfProductProperty() {
        return totalPriceOfProduct;
    }
    private double round(double value) {
        return new BigDecimal(value).setScale(5, RoundingMode.HALF_UP).doubleValue();
    }
}
