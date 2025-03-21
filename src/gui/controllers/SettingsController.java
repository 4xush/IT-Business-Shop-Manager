package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import database.DatabaseConnection;

import java.io.File;
import java.io.IOException;

public class SettingsController {

    @FXML private Button exportButton;
    @FXML private Button loadButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        // Nothing to initialize yet
    }

    @FXML
    private void exportDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Database");
        fileChooser.setInitialFileName("kingcom_backup.db");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database", "*.db"));
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());
        if (file != null) {
            try {
                DatabaseConnection.exportDatabase(file);
                showAlert("Success", "Database exported to " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "Failed to export database: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void loadDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Database");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database", "*.db"));
        File file = fileChooser.showOpenDialog(loadButton.getScene().getWindow());
        if (file != null) {
            try {
                DatabaseConnection.loadDatabase(file);
                showAlert("Success", "Database loaded from " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "Failed to load database: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void backToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Dashboard.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopSync Manager Dashboard");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to return to dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}