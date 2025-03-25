package com.example.esepkersoft.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private PasswordField passwordField;

    private static final String CORRECT_PASSWORD = "12345";

    @FXML
    private void handleLoginButton() {
        String password = passwordField.getText();
        
        if (password.equals(CORRECT_PASSWORD)) {
            try {
                // Load the main application view (SalesPoint.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/esepkersoft/Views/SalesPoint.fxml"));
                Parent root = loader.load();
                
                // Create new scene and stage
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Sales Point");
                stage.setMaximized(true); // Make the SalesPoint view full screen
                stage.show();
                
                // Close the login window
                Stage loginStage = (Stage) passwordField.getScene().getWindow();
                loginStage.close();
            } catch (Exception e) {
                showAlert("Error", "Failed to open the application: " + e.getMessage());
            }
        } else {
            showAlert("Login Failed", "Incorrect password. Please try again.");
            passwordField.clear(); // Clear the password field on incorrect attempt
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 