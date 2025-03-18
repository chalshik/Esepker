package com.example.esepkersoft.Services;

import java.util.List;
import java.util.Map;
 public class ProductOperations {
    public boolean AddProductToDB(String barcode,String date,double exPrice,double inPrice){
        return true;
    }
    public static List<Map<String, Object>> getProductByBarcode(String barcode) {
        dbManager db = dbManager.getInstance();
        // Construct the query string manually
        String query = "SELECT * FROM products WHERE barcode = '" + barcode + "'";
        List<Map<String, Object>> data = db.executeGet(query, transactionId, barcode);
        return data;
    }
}
