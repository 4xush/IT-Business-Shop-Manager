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
import gui.controllers.ProductFormController;
import java.util.List;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;

    private ProductService productService = new ProductService();
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
        List<Product> products = productService.getAllProducts();
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
            showAlert("No Product Selected", "Please select a product to update.");
        }
    }

    @FXML
    private void deleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ProductService.deleteProduct(selected.getId());  // Static call
            loadProductData();
        } else {
            showAlert("No Product Selected", "Please select a product to delete.");
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
            showAlert("Error", "Failed to open product form: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
