package gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Product;
import services.ProductService;

import java.io.IOException;
import java.util.List;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
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
            ProductService.deleteProduct(selected.getId());  // Static call
            loadProductData();
        } else {
            showAlert("No Product Selected", "Please select a product to delete.", Alert.AlertType.WARNING);
        }
    }

    private void openProductForm(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ProductForm.fxml"));
            AnchorPane root = loader.load();
            ProductFormController controller = loader.getController();
            controller.setProduct(product, this);
            Stage stage = new Stage();
            stage.setTitle(product == null ? "Add Product" : "Update Product");
            stage.setScene(new Scene(root));
            
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to open product form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void backToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Dashboard.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) productTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("IT Service Shop Dashboard");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
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