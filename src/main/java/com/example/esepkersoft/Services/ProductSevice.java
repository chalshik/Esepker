package com.example.esepkersoft.Services;

import com.example.esepkersoft.Models.Product;

import java.util.List;
import java.util.Map;

public class ProductSevice {
    dbManager db = dbManager.getInstance();
    public static Product getProductByBarcode(String barcode) {
        Product p = null;
        dbManager db = dbManager.getInstance();
        String query = "SELECT * FROM products WHERE barcode = ?";
        List<Map<String, Object>> results = db.executeGet(query, barcode);
        if (!results.isEmpty()) {
            Map<String, Object> productData = results.get(0);
            String priceStr = (String) productData.get("price");
            double price = Double.parseDouble(priceStr);
            p = new Product(
                    (String) productData.get("barcode"),
                    (String) productData.get("name"),
                    (String) productData.get("type"),
                    price,
                    (String) productData.get("unit_measurement")
            );
        }
        return p;
    }
    public static boolean addProduct(String barcode, String name, String type, double price, String unitMeasurement) {
        if (barcode == null || barcode.isEmpty() || name == null || name.isEmpty() || price <= 0) {
            return false;  // Basic validation failed
        }
        if (!"measurable".equals(unitMeasurement) && !"countable".equals(unitMeasurement)) {
            unitMeasurement = "countable";  // Default to countable if invalid value
        }
        dbManager db = dbManager.getInstance();
        String query = "INSERT INTO products (barcode, name, type, price, unit_measurement) VALUES (?, ?, ?, ?, ?)";
        String priceStr = String.valueOf(price);
        return db.executeSet(query, barcode, name, type, priceStr, unitMeasurement);
    }
    public static boolean updateProductPrice(String barcode, double newPrice) {
        if (barcode == null || barcode.isEmpty() || newPrice <= 0) {
            return false;  // Basic validation failed
        }
        dbManager db = dbManager.getInstance();
        String query = "UPDATE products SET price = ? WHERE barcode = ?";
        String priceStr = String.valueOf(newPrice);
        return db.executeSet(query, priceStr, barcode);
    }
    public static boolean deleteProduct(String barcode) {
        // Validate input
        if (barcode == null || barcode.isEmpty()) {
            return false;  // Basic validation failed
        }
        dbManager db = dbManager.getInstance();
        String query = "DELETE FROM products WHERE barcode = ?";
        return db.executeSet(query, barcode);
    }
}
