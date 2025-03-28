package gui.controllers;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Product;
import services.ProductService;
import java.util.List;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        loadProductData();
    }

    public void loadProductData() {
        List<Product> products = ProductService.getAllProducts();
        productList.setAll(products);
        productTable.setItems(productList);
    }

    @FXML
    private void addProduct() {
        openProductForm(null);
    }

    @FXML
    private void updateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openProductForm(selected);
        } else {
            showAlert("No Product Selected", "Please select a product to update.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void deleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ProductService.deleteProduct(selected.getId());
            loadProductData();
        } else {
            showAlert("No Product Selected", "Please select a product to delete.", Alert.AlertType.WARNING);
        }
    }

    private void openProductForm(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductForm.fxml"));
            Parent root = loader.load();
            ProductFormController controller = loader.getController();
            controller.setProduct(product, this);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            // Set window properties
            stage.setTitle(product == null ? "Add Product" : "Update Product");
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            
            // Center on screen
            stage.centerOnScreen();
            
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to open product form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void backToDashboard() {
        try {
            Stage stage = (Stage) productTable.getScene().getWindow();
            DashboardController.returnToDashboard(stage, hostServices);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
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