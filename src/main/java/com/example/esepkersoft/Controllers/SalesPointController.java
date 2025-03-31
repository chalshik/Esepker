package com.example.esepkersoft.Controllers;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import com.example.esepkersoft.Models.Sales;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.esepkersoft.Services.ProductOperations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SalesPointController {
    @FXML
    private Button addToCartbtn;
    @FXML
    private Button scanBarcodebtn;

    @FXML
    private TextField barcodeField;

    @FXML
    private VBox productDetailsPane;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productPriceLabel;

    @FXML
    private TextField quantityField;

    @FXML
    private ComboBox<String> measureUnitCombo;

    @FXML
    private TableView<Sales> cartTable;

    @FXML
    private TableColumn<Sales, String> barcodeColumn;

    @FXML
    private TableColumn<Sales, String> nameColumn;

    @FXML
    private TableColumn<Sales, Double> quantityColumn;

    @FXML
    private TableColumn<Sales, Double> priceColumn;

    @FXML
    private TableColumn<Sales, Double> totalColumn;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private RadioButton cashRadio;

    @FXML
    private RadioButton cardRadio;

    @FXML
    private VBox cashPaymentPane;

    @FXML
    private TextField receivedAmountField;

    @FXML
    private Label changeLabel;

    @FXML
    private ToggleGroup cashOrCardToggle;

    @FXML
    private Button calculateChangebtn;

    @FXML
    private Button commitbtn;

    @FXML
    private Label totalOfProductLabel;
    private double lastProductsPrice = (double) 0;
    private ObservableList<Sales> salesList = FXCollections.observableArrayList();
    private String lastEnteredBarcode = " ";
    private double lastProductsAmount = 0;
    private String lastSelectedRadioButton = " ";
    @FXML
    private void initialize(){
        cashOrCardToggle = new ToggleGroup();
        cashRadio.setToggleGroup(cashOrCardToggle);
        cardRadio.setToggleGroup(cashOrCardToggle);
        // when barcode entered
        barcodeField.setOnAction(event -> {
            scanBarcode();
        });

        scanBarcodebtn.setOnAction(event ->{
            scanBarcode();
        });
        // disabling combo box
        measureUnitCombo.setDisable(true);

        measureUnitCombo.getItems().addAll("kg", "g");

        quantityField.setOnAction(event -> {
            try {
                double quantity = Double.parseDouble(quantityField.getText().trim());

                // Ensure quantity is non-negative
                if (quantity < 0) {
                    totalOfProductLabel.setText("Ошибка: количество < 0");
                    return;
                }
                if ("g".equals(measureUnitCombo.getValue())) {
                    quantity = quantity / 1000;
                }
                lastProductsAmount = quantity;
                double total = quantity * lastProductsPrice;

                // Format output to avoid unnecessary ".0"
                totalOfProductLabel.setText(String.format("Итого за продукт: %.2f", total));
            } catch (NumberFormatException e) {
                totalOfProductLabel.setText("Ошибка: неверный ввод");
            }
        });


        measureUnitCombo.setOnAction(event -> {
            try {
                double quantity = Double.parseDouble(quantityField.getText().trim());

                // Ensure quantity is non-negative
                if (quantity < 0) {
                    totalOfProductLabel.setText("Ошибка: количество < 0");
                    return;
                }

                // Calculate the total based on the selected measure unit
                double total;
                if ("g".equals(measureUnitCombo.getValue())) {
                    total = lastProductsPrice * (quantity / 1000);
                    // Convert to kilograms if "g"
                    quantity = quantity/1000;

                } else {
                    total = lastProductsPrice * quantity; // Regular price calculation
                }
                lastProductsAmount = quantity;
                // Round to 2 decimal places
                BigDecimal roundedTotal = BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
                totalOfProductLabel.setText("Итого за продукт: " + roundedTotal);

            } catch (NumberFormatException e) {
                totalOfProductLabel.setText("Ошибка: неверный ввод");
            }
        });
        scanBarcodebtn.setOnAction(event -> {

        });

        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceForPiece"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPriceOfProduct"));
        cartTable.setItems(salesList);
        addToCartbtn.setOnAction(event -> {
            addToCart();
        });
        quantityField.setOnAction(event -> {
            addToCart();
        });
        receivedAmountField.setOnAction(event -> {
            calculateChange();
        });
        calculateChangebtn.setOnAction(event -> {
            calculateChange();
        });
        receivedAmountField.setDisable(true);
        calculateChangebtn.setDisable(true);
        cashOrCardToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selectedRadio = (RadioButton) newToggle;
                if ("Cash".equals(selectedRadio.getText())) {
                    receivedAmountField.setText(null);
                    receivedAmountField.setDisable(false);
                    changeLabel.setText(null);
                    calculateChangebtn.setDisable(false);
                    lastSelectedRadioButton = "cash";
                } else {
                    receivedAmountField.setText(null);
                    receivedAmountField.setDisable(true);
                    changeLabel.setText(null);
                    calculateChangebtn.setDisable(true);
                    lastSelectedRadioButton = "card";
                }
            }
        });
        commitbtn.setOnAction(event -> {
            commitSale();
        });

    }
    private void calculateChange() {
        double receivedAmount = Double.parseDouble(receivedAmountField.getText());
        changeLabel.setText(String.valueOf(receivedAmount - Double.parseDouble(totalAmountLabel.getText())));
    }
    private void addToCart() {
        Sales existingSale = productExistsInTable(lastEnteredBarcode);
        if (existingSale != null) {
            // Update the existing sale's quantity and total price
            existingSale.setAmount(existingSale.getAmount() + lastProductsAmount);
            cartTable.refresh(); // Refresh table view to show updated values
        } else {
            Sales sale = new Sales(lastEnteredBarcode, productNameLabel.getText(), lastProductsAmount, lastProductsPrice);
            salesList.add(sale);
        }
        totalAmountLabel.setText(totalAmountOnCart());

    }
    private String totalAmountOnCart() {
        Double total = (double) 0;
        for (Sales sale : cartTable.getItems()) {
            total += sale.getTotalPriceOfProduct();
        }
        return String.valueOf(total);
    }

    private Sales productExistsInTable(String barcode) {
        for (Sales sale : cartTable.getItems()) {
            if (sale.getBarcode().equals(barcode)) {
                return sale;
            }
        }
        return null;
    }

    private void scanBarcode(){
        String query = barcodeField.getText().trim(); // Trim input to avoid spaces
        HashMap<String, Object> productDetails = ProductOperations.getProductByBarcode(query);

        if (productDetails == null || productDetails.isEmpty()) {
            productNameLabel.setText("Продукт не найден");
            productPriceLabel.setText("Цена: -");
            totalOfProductLabel.setText("Итого за продукт: -");
            quantityField.setText("");
            measureUnitCombo.setDisable(true); // Ensure combobox is disabled if product is not found
            quantityField.setDisable(true);
            addToCartbtn.setDisable(true);
            barcodeField.clear();
            return;
        }

        productNameLabel.setText((String) productDetails.getOrDefault("name", "Неизвестный продукт"));

        // Convert price safely
        Object priceObject = productDetails.get("price");
        double price = priceObject instanceof Double ? (Double) priceObject : Double.parseDouble(priceObject.toString());

        productPriceLabel.setText("Цена: " + price);
        totalOfProductLabel.setText("Итого за продукт: " + price);
        quantityField.setText("1");
        lastProductsAmount = 1;
        // Disable combo box by default
        measureUnitCombo.setDisable(true);

        // Enable combo box if product is measurable
        if ("measurable".equals(String.valueOf(productDetails.get("type")))) {
            measureUnitCombo.setDisable(false);
        }

        lastProductsPrice = price;
        // Store price correctly
        lastEnteredBarcode = barcodeField.getText();
        barcodeField.clear();
        quantityField.setDisable(false);
        addToCartbtn.setDisable(false);
    }
    private void commitSale() {
        List<HashMap<String, Object>> listOfSaleMap = new ArrayList<>();
        for (Sales sale : cartTable.getItems()) {
            HashMap<String, Object> saleMap = new HashMap<>();
            saleMap.put("barcode", sale.getBarcode());
            saleMap.put("name", sale.getName());
            saleMap.put("quantity", sale.getAmount());
            saleMap.put("price", sale.getPriceForPiece());
            saleMap.put("total_price_of_product", sale.getTotalPriceOfProduct());
            listOfSaleMap.add(saleMap);
        }
        HashMap<String, Object> paymentMethod = new HashMap<>();

        paymentMethod.put("payment_method", lastSelectedRadioButton);


        ProductOperations.insertToDatabase(listOfSaleMap,lastSelectedRadioButton, totalAmountLabel.getText());
    }

}


