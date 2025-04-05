package com.example.esepkersoft.Services;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbManager {
    // Singleton instance
    private static dbManager instance;
    private Connection connection;

    // Private constructor to prevent instantiation
    private dbManager() {
        connectDB();
    }

    // Singleton getInstance method
    public static synchronized dbManager getInstance() {
        if (instance == null) {
            instance = new dbManager();
        }
        return instance;
    }

    // Connect to SQLite database
    private boolean connectDB() {
        try {
            // SQLite connection URL (local file)
            String url = "jdbc:sqlite:shop.db"; // Database file will be created in the project root
            connection = DriverManager.getConnection(url);
            System.out.println("Database connected successfully!");
            createTables();
            return true;  // Return true if connection is successful
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            openError();
            return false;  // Return false if connection fails
        }
    }

    // Check if the database is connected
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Handle reload (reconnect)
    public boolean onReload() {
        return connectDB();  // Simply return the result of connectDB()
    }

    // Show error (replace with your UI logic)
    private void openError() {
        System.err.println("Database error occurred.");
        // Example: Show a dialog or retry logic
    }

    // Execute a SET query (INSERT, UPDATE, DELETE)
    // Modified executeGet to accept parameters for prepared statement
    public List<Map<String, Object>> executeGet(String query, Object... params) {
        List<Map<String, Object>> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                result.add(row);
            }
        } catch (SQLException e) {
            System.err.println("GET query error: " + e.getMessage());
            openError();
        }
        return result;
    }

    // Modified executeSet to accept parameters for prepared statement
    public boolean executeSet(String query, Object... params) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("SET query error: " + e.getMessage());
            openError();
            return false;
        }
    }


    // Create necessary tables
    // Create all tables
    private void createTables() {
        createProductsTable();
        createSalesTable();
        createSaleItemsTable();
    }

    private void createProductsTable() {
        String query = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "barcode TEXT NOT NULL UNIQUE, "
                + "name TEXT NOT NULL, "
                + "type TEXT, "
                + "price TEXT NOT NULL, "
                + "unit_measurement TEXT NOT NULL DEFAULT 'countable' "
                + "CHECK(unit_measurement IN ('measurable', 'countable')))";
        executeSet(query);
    }

    private void createSalesTable() {
        String query = "CREATE TABLE IF NOT EXISTS sales ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "date TEXT DEFAULT (datetime('now', 'localtime')), "
                + "payment_method TEXT NOT NULL, "
                + "total TEXT NOT NULL)";
        executeSet(query);
    }

    private void createSaleItemsTable() {
        String query = "CREATE TABLE IF NOT EXISTS sale_items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "barcode TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "quantity REAL NOT NULL, "
                + "price REAL NOT NULL, "
                + "total_price REAL NOT NULL, "
                + "sale_id INTEGER NOT NULL, "
                + "FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE)";
        executeSet(query);
    }
}