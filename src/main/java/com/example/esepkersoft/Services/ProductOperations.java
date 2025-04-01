package com.example.esepkersoft.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ProductOperations {
    public static HashMap<String, Object> getProductByBarcode(String barcode) {
        dbManager db = dbManager.getInstance();
        // Using a prepared statement to prevent SQL injection
        String query = "SELECT name, type, price FROM products WHERE barcode = ?";
        List<Map<String, Object>> productDetailsInListMap = db.executeGet(query, barcode);

        // Check if the list is empty or null before accessing the first element
        if (productDetailsInListMap == null || productDetailsInListMap.isEmpty()) {
            return new HashMap<>();  // Return an empty map if no product found
        }

        return new HashMap<>(productDetailsInListMap.get(0));
    }

    public static void insertToDatabase(List<HashMap<String, Object>> saleDetails, String paymentMethod, String total) {
        dbManager db = dbManager.getInstance();
        String saleId = getSaleId(paymentMethod, total);
        for (HashMap<String, Object> saleRow : saleDetails) {
            // Retrieve sale details
            Object barcode = saleRow.get("barcode");
            Object name = saleRow.get("name");
            Object price = saleRow.get("price");
            Object quantity = saleRow.get("quantity");
            Object totalPriceOfProduct = saleRow.get("total_price_of_product");

            // Insert sale item
            insertSaleItem(barcode, name, price, quantity, totalPriceOfProduct, saleId);
        }
    }

    private static String getSaleId(String paymentMethod, String total) {
        dbManager db = dbManager.getInstance();
        String query = "INSERT INTO sales (date, payment_method, total) VALUES (CURRENT_TIMESTAMP, ?, ?)";
        db.executeSet(query, paymentMethod, total);

        // Get the last inserted sale ID
        List<Map<String, Object>> result = db.executeGet("SELECT last_insert_rowid();");
        if (!result.isEmpty()) {
            Object value = result.get(0).get("last_insert_rowid()");
            return value != null ? value.toString() : "";
        }
        return "";
    }

    private static void insertSaleItem(Object barcode, Object name, Object price, Object quantity, Object totalPriceOfProduct, Object saleId) {
        dbManager db = dbManager.getInstance();
        String query = "INSERT INTO sale_items (barcode, name, price, quantity, total_price_of_product, sale_id) VALUES (?, ?, ?, ?, ?, ?)";
        // Use prepared statement to safely insert values
        db.executeSet(query, barcode, name, price, quantity, totalPriceOfProduct, saleId);
        System.out.println("Sale item inserted successfully.");
    }

}

