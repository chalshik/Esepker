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
    @FXML private Button addToCartbtn;
    @FXML private Button scanBarcodebtn;
    @FXML private TextField barcodeField;
    @FXML private VBox productDetailsPane;
    @FXML private Label productNameLabel;
    @FXML private Label productPriceLabel;
    @FXML private TextField quantityField;
    @FXML private ComboBox<String> measureUnitCombo;
    @FXML private TableView<Sales> cartTable;
    @FXML private TableColumn<Sales, String> barcodeColumn;
    @FXML private TableColumn<Sales, String> nameColumn;
    @FXML private TableColumn<Sales, Double> quantityColumn;
    @FXML private TableColumn<Sales, Double> priceColumn;
    @FXML private TableColumn<Sales, Double> totalColumn;
    @FXML private Label totalAmountLabel;
    @FXML private RadioButton cashRadio;
    @FXML private RadioButton cardRadio;
    @FXML private VBox cashPaymentPane;
    @FXML private TextField receivedAmountField;
    @FXML private Label changeLabel;
    @FXML private ToggleGroup cashOrCardToggle;
    @FXML private Button calculateChangebtn;
    @FXML private Button commitbtn;
    @FXML private Label labelForErrorCommits;
    @FXML private Label totalOfProductLabel;

    // State variables
    private double lastProductsPrice = 0;
    private ObservableList<Sales> salesList = FXCollections.observableArrayList();
    private String lastEnteredBarcode = "";
    private double lastProductsAmount = 0;
    // Using "cash" or "card" to indicate payment method
    private String lastSelectedRadioButton = null;

    @FXML
    private void initialize() {
        // Hide error label initially
        labelForErrorCommits.setVisible(false);

        // Configure toggle group for payment methods
        cashOrCardToggle = new ToggleGroup();
        cashRadio.setToggleGroup(cashOrCardToggle);
        cardRadio.setToggleGroup(cashOrCardToggle);

        // Barcode scanning
        barcodeField.setOnAction(event -> scanBarcode());
        scanBarcodebtn.setOnAction(event -> scanBarcode());

        // Disable measure unit combobox by default
        measureUnitCombo.setDisable(true);
        measureUnitCombo.getItems().addAll("kg", "g");

        // Quantity field action to update total for product
        quantityField.setOnAction(event -> updateProductTotal());
        measureUnitCombo.setOnAction(event -> updateProductTotal());

        // Configure table columns
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode figna"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceForPiece"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPriceOfProduct"));
        cartTable.setItems(salesList);

        // Add to cart actions
        addToCartbtn.setOnAction(event -> addToCart());
        quantityField.setOnAction(event -> updateProductTotal() );

        // Configure change calculation
        receivedAmountField.setOnAction(event -> calculateChange());
        calculateChangebtn.setOnAction(event -> calculateChange());
        receivedAmountField.setDisable(true);
        calculateChangebtn.setDisable(true);

        // Payment method selection listener
        cashOrCardToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selectedRadio = (RadioButton) newToggle;
                if ("Cash".equalsIgnoreCase(selectedRadio.getText())) {
                    // Prepare for cash payment
                    receivedAmountField.clear();
                    receivedAmountField.setDisable(false);
                    changeLabel.setText("");
                    calculateChangebtn.setDisable(false);
                    lastSelectedRadioButton = "cash";
                } else if ("Card".equalsIgnoreCase(selectedRadio.getText())) {
                    // Prepare for card payment
                    clearChangeFields();
                    lastSelectedRadioButton = "card";
                }
            } else {
                lastSelectedRadioButton = null;
            }
        });

        // Commit sale action
        commitbtn.setOnAction(event -> commitSale());
    }

    // Calculate total price for the current product based on quantity and unit selection
    private void updateProductTotal() {
        try {
            double quantity = Double.parseDouble(quantityField.getText().trim());
            if (quantity < 0) {
                totalOfProductLabel.setText("Ошибка: количество < 0");
                return;
            }
            // If unit is grams, convert to kilograms
            if ("g".equals(measureUnitCombo.getValue())) {
                quantity = quantity / 1000;
            }
            lastProductsAmount = quantity;
            double total = quantity * lastProductsPrice;
            totalOfProductLabel.setText(String.format("Итого за продукт: %.2f", total));
        } catch (NumberFormatException e) {
            totalOfProductLabel.setText("Ошибка: неверный ввод");
        }
    }

    // Calculate and display change for cash payments
    private void calculateChange() {
        try {
            double receivedAmount = Double.parseDouble(receivedAmountField.getText());
            double totalAmount = Double.parseDouble(totalAmountLabel.getText());
            double change = receivedAmount - totalAmount;
            if (change < 0) {
                changeLabel.setText("Еще не хватает: " + change);
            } else {
                changeLabel.setText(String.valueOf(change));
            }
        } catch (NumberFormatException e) {
            showError("Ошибка при вводе суммы");
        }
    }

    // Add product to the cart table
    private void addToCart() {
        Sales existingSale = productExistsInTable(lastEnteredBarcode);
        if (existingSale != null) {
            // Update quantity and total price if product already exists in cart
            existingSale.setAmount(existingSale.getAmount() + lastProductsAmount);
            cartTable.refresh();
        } else {
            Sales sale = new Sales(lastEnteredBarcode, productNameLabel.getText(), lastProductsAmount, lastProductsPrice);
            salesList.add(sale);
        }
        totalAmountLabel.setText(totalAmountOnCart());
        // Reset product details display
        productNameLabel.setText("-");
        productPriceLabel.setText("Цена: -");
        totalOfProductLabel.setText("Итого за продукт: -");
        quantityField.clear();
        measureUnitCombo.setDisable(true);
        quantityField.setDisable(true);
        addToCartbtn.setDisable(true);
    }

    // Calculate total amount for all products in cart
    private String totalAmountOnCart() {
        double total = 0;
        for (Sales sale : cartTable.getItems()) {
            total += sale.getTotalPriceOfProduct();
        }
        return String.valueOf(total);
    }

    // Check if product with the given barcode already exists in the cart
    private Sales productExistsInTable(String barcode) {
        for (Sales sale : cartTable.getItems()) {
            if (sale.getBarcode().equals(barcode)) {
                return sale;
            }
        }
        return null;
    }

    // Scan barcode and update product details
    private void scanBarcode() {
        String query = barcodeField.getText().trim();
        HashMap<String, Object> productDetails = ProductOperations.getProductByBarcode(query);

        if (productDetails == null || productDetails.isEmpty()) {
            productNameLabel.setText("Продукт не найден");
            productPriceLabel.setText("Цена: -");
            totalOfProductLabel.setText("Итого за продукт: -");
            quantityField.clear();
            measureUnitCombo.setDisable(true);
            quantityField.setDisable(true);
            addToCartbtn.setDisable(true);
            barcodeField.clear();
            return;
        }

        productNameLabel.setText((String) productDetails.getOrDefault("name", "Неизвестный продукт"));
        Object priceObject = productDetails.get("price");
        double price = priceObject instanceof Double ? (Double) priceObject : Double.parseDouble(priceObject.toString());
        productPriceLabel.setText("Цена: " + price);
        totalOfProductLabel.setText("Итого за продукт: " + price);
        quantityField.setText("1");
        lastProductsAmount = 1;
        measureUnitCombo.setDisable(true);

        // Enable measure unit if product is measurable
        if ("measurable".equals(String.valueOf(productDetails.get("type")))) {
            measureUnitCombo.setDisable(false);
            System.out.println("type is measurable");
        }

        lastProductsPrice = price;
        lastEnteredBarcode = barcodeField.getText();
        barcodeField.clear();
        quantityField.setDisable(false);
        addToCartbtn.setDisable(false);
    }

    // Commit the sale process
    private void commitSale() {
        // Check if the cart is empty
        if (salesList.isEmpty()) {
            showError("Корзина пуста");
            return;
        }

        // Check if a payment method is selected
        if (lastSelectedRadioButton == null) {
            showError("Выберите вид оплаты");
            return;
        }

        // Process cash payments
        if ("cash".equals(lastSelectedRadioButton)) {
            try {
                double change = Double.parseDouble(changeLabel.getText());
                if (change < 0) {
                    showError("Не хватает средств");
                    return;
                }
                processSale();
            } catch (NumberFormatException e) {
                showError("Ошибка ввода суммы");
            }
        }
        // Process card payments
        else if ("card".equals(lastSelectedRadioButton)) {
            processSale();
        } else {
            showError("Неизвестный метод оплаты");
        }
    }

    // Process sale: insert data into the database and clear UI fields
    private void processSale() {
        List<HashMap<String, Object>> listOfSaleMap = getSaleItems();
        ProductOperations.insertToDatabase(listOfSaleMap, lastSelectedRadioButton, totalAmountLabel.getText());
        clearForCommit();
        // Deselect any payment method from the toggle group
        cashOrCardToggle.selectToggle(null);
        clearChangeFields();
    }

    // Create a list of sale items for database insertion
    private List<HashMap<String, Object>> getSaleItems() {
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
        return listOfSaleMap;
    }

    // Display an error message to the user
    private void showError(String message) {
        labelForErrorCommits.setText(message);
        labelForErrorCommits.setVisible(true);
    }

    // Clear all fields and reset the UI after a successful commit
    private void clearForCommit() {
        barcodeField.clear();
        productNameLabel.setText("-");
        productPriceLabel.setText("Цена: -");
        totalOfProductLabel.setText("Итого за продукт: -");
        quantityField.clear();
        measureUnitCombo.setDisable(true);
        quantityField.setDisable(true);
        addToCartbtn.setDisable(true);
        salesList.clear();
        cartTable.refresh();
        totalAmountLabel.setText("0");
    }

    // Clear change-related fields for cash payments
    private void clearChangeFields() {
        receivedAmountField.clear();
        receivedAmountField.setDisable(true);
        changeLabel.setText("");
        calculateChangebtn.setDisable(true);
        // Reset payment method if needed (optional)
        // lastSelectedRadioButton = null;
    }
}
