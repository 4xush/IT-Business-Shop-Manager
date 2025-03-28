package gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Product;
import services.ProductService;

public class ProductFormController {

    @FXML private TextField productNameField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private Button addButton;
    @FXML private Button updateButton;

    private ProductController productController;
    private Product currentProduct;

    @FXML
    public void initialize() {
        // Populate ComboBox with predefined categories
        categoryComboBox.setItems(FXCollections.observableArrayList(
            "Phones", "Headphones", "Laptops", "Charger"
        ));
    }

    public void setProduct(Product product, ProductController controller) {
        this.productController = controller;
        this.currentProduct = product;
        if (product != null) {
            productNameField.setText(product.getName());
            categoryComboBox.setValue(product.getCategory());
            priceField.setText(String.valueOf(product.getPrice()));
            stockField.setText(String.valueOf(product.getStock()));
            addButton.setDisable(true);
            updateButton.setDisable(false);
        } else {
            clearFields();
            addButton.setDisable(false);
            updateButton.setDisable(true);
        }
    }

    @FXML
    private void addProduct() {
        if (validateInputs()) {
            String name = productNameField.getText();
            String category = categoryComboBox.getEditor().getText().trim();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            ProductService.addProduct(name, category, price, stock);
            showAlert("Success", "Product added successfully!", Alert.AlertType.INFORMATION);
            if (productController != null)
                productController.loadProductData();
            clearFields();
        }
    }

    @FXML
    private void updateProduct() {
        if (currentProduct == null) {
            showAlert("Warning", "No product to update!", Alert.AlertType.WARNING);
            return;
        }
        if (validateInputs()) {
            int id = currentProduct.getId();
            String name = productNameField.getText();
            String category = categoryComboBox.getEditor().getText().trim();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            ProductService.updateProduct(id, name, category, price, stock);
            showAlert("Success", "Product updated successfully!", Alert.AlertType.INFORMATION);
            if (productController != null)
                productController.loadProductData();
            clearFields();
        }
    }

    private boolean validateInputs() {
        if (productNameField.getText().trim().isEmpty() ||
            categoryComboBox.getEditor().getText().trim().isEmpty() ||
            priceField.getText().trim().isEmpty() ||
            stockField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Double.parseDouble(priceField.getText());
            Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Price must be a valid number and Stock must be an integer!",
                    Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void clearFields() {
        productNameField.clear();
        categoryComboBox.setValue(null);
        categoryComboBox.getEditor().clear();
        priceField.clear();
        stockField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) productNameField.getScene().getWindow();
        stage.close();
    }
}