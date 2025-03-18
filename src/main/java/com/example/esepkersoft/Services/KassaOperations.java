package com.example.esepkersoft.Services;

import java.util.*;

public class KassaOperations {
    String transactionId;
    dbManager db = dbManager.getInstance();
    void createTransaction(){
        String insertQuery = "INSERT INTO transactions DEFAULT VALUES";
        boolean insertSuccess = db.executeSet(insertQuery);

        if (!insertSuccess) {
            throw new RuntimeException("Failed to insert transaction");
        }
        String getQuery = "SELECT last_insert_rowid() AS id"; // Alias column as 'id'
        List<Map<String,Object>> result = db.executeGet(getQuery);
        if (result.isEmpty()){
            throw new RuntimeException("Failed to retrieve transaction ID");
        }
        this.transactionId = ((Number) result.get(0).get("id")).toString();
    }
    List<Map<String, Object>> getProductByBarcode(String barcode) {
        // Fetch product details from products table
        String query = "SELECT name, type, price FROM products WHERE barcode = '" + barcode + "';";
        List<Map<String, Object>> productDetails = db.executeGet(query);

        if (productDetails.isEmpty()) {
            System.err.println("Error: No product found for barcode: " + barcode);
            return new ArrayList<>();
        }

        // Assuming productDetails has only one product for the barcode:
        Map<String, Object> product = productDetails.get(0);
        String name = (String) product.get("name");
        String type = (String) product.get("type");
        String price = (String) product.get("price");

        // Check if barcode was already scanned using some helper method.
        // (This check might be done by querying the transactions table or by maintaining a set in your UI.)
        if (isBarcodeAlreadyScanned(barcode)) {
            // Example update: Increase quantity of the existing transaction row
            String updateQuery = "UPDATE transactions SET quantity = quantity + 1 " +
                    "WHERE transaction_id = ? AND name = ?";
            db.executeSet(updateQuery, transactionId, name);

            // Optionally, update the local product map with new quantity
            product.put("quantity", getUpdatedQuantity(transactionId, name)); // your helper method to fetch the new quantity
        } else {
            // If not already scanned, insert a new transaction row
            String insertRowQuery = "INSERT INTO transactions (transaction_id, name, quantity, price) VALUES (?,?,?,?)";
            db.executeSet(insertRowQuery, transactionId, name, "1", price);
            product.put("quantity", 1);
        }

        // Optionally, you might want to return a list containing this product details
        // or merge it with other previously scanned products.
        return productDetails;
    }

// Example helper methods (you must implement these according to your design):

    /**
     * Checks if a product with the given barcode is already scanned in the current transaction.
     */
    boolean isBarcodeAlreadyScanned(String barcode) {
        // This could involve checking an in-memory set or querying the transactions table.
        // For example:
        String query = "SELECT COUNT(*) AS count FROM transactions " +
                "WHERE transaction_id = ? AND barcode = ?";
        List<Map<String, Object>> result = db.executeGet(query, transactionId, barcode);
        if (!result.isEmpty()) {
            Number count = (Number) result.get(0).get("count");
            return count.intValue() > 0;
        }
        return false;
    }

    /**
     * Retrieves the updated quantity for the product in the transaction.
     */
    int getUpdatedQuantity(String transactionId, String productName) {
        String query = "SELECT quantity FROM transactions WHERE transaction_id = ? AND name = ?";
        List<Map<String, Object>> result = db.executeGet(query, transactionId, productName);
        if (!result.isEmpty()) {
            return ((Number) result.get(0).get("quantity")).intValue();
        }
        return 1;
    }

}
