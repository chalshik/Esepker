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
    public boolean executeSet(String query) {
        try (Statement statement = connection.createStatement()) {
            System.out.println("Executing SET query: " + query);
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            System.err.println("SET query error: " + e.getMessage());
            openError();
            return false;
        }
    }

    // Execute a GET query (SELECT)
    public List<Map<String, Object>> executeGet(String query) {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("Executing GET query: " + query);
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

    // Create necessary tables
    private void createTables() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "barcode TEXT NOT NULL UNIQUE, "
                + "name TEXT NOT NULL)";
        executeSet(createTableQuery);
    }
}