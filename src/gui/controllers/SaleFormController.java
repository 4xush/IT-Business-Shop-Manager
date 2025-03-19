package gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Product;
import models.Sale;
import services.ProductService;
import services.SaleService;
import java.util.List;
public class SaleFormController {

    @FXML private ComboBox<Product> productCombo;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField quantityField;
    @FXML private TableView<Sale> cartTable;
    @FXML private TableColumn<Sale, Integer> colProductId;
    @FXML private TableColumn<Sale, Integer> colQuantity;
    @FXML private TableColumn<Sale, Double> colTotalAmount;
    @FXML private Label totalLabel;
    @FXML private Button addToCartButton;
    @FXML private Button confirmButton;

    private SaleController saleController;
    private Sale currentSale; // For update mode
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Sale> cartList = FXCollections.observableArrayList();
    private int saleGroupId = -1;

    @FXML
    public void initialize() {
        categoryCombo.setItems(FXCollections.observableArrayList("All", "Phones", "Headphones")); // Adjust categories
        categoryCombo.setValue("All");
        loadProducts("All");

        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        cartTable.setItems(cartList);

        categoryCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadProducts(newVal);
        });

        updateTotal();
    }

    private void loadProducts(String category) {
        productList.clear();
        List<Product> products = ProductService.getAllProducts(); // Assuming this exists
        if (!"All".equals(category)) {
            products.removeIf(p -> !category.equals(p.getCategory()));
        }
        productList.addAll(products);
        productCombo.setItems(productList);
        productCombo.setCellFactory(lv -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName() + " (ID: " + item.getId() + ", Stock: " + item.getStock() + ")");
            }
        });
        productCombo.setButtonCell(new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });
    }

    public void setSale(Sale sale, SaleController controller) {
        this.saleController = controller;
        this.currentSale = sale;
        if (sale != null) {
            // Update mode: Load single item
            Product product = ProductService.getProductById(sale.getProductId());
            productCombo.setValue(product);
            quantityField.setText(String.valueOf(sale.getQuantity()));
            productCombo.setDisable(true);
            addToCartButton.setDisable(true);
            confirmButton.setDisable(true);
            cartTable.setVisible(false);
            totalLabel.setVisible(false);
        } else {
            // Add mode: New sale group
            saleGroupId = SaleService.createSaleGroup();
            if (saleGroupId == -1) {
                showAlert("Error", "Failed to create sale group!", Alert.AlertType.ERROR);
                closeWindow();
            }
            clearFields();
            productCombo.setDisable(false);
            addToCartButton.setDisable(false);
            confirmButton.setDisable(false);
            cartTable.setVisible(true);
            totalLabel.setVisible(true);
        }
    }

    @FXML
    private void addToCart() {
        if (validateInputs()) {
            Product selectedProduct = productCombo.getValue();
            int quantity = Integer.parseInt(quantityField.getText());
            if (SaleService.addSaleItem(saleGroupId, selectedProduct.getId(), quantity)) {
                Sale sale = new Sale(saleGroupId, selectedProduct.getId(), quantity);
                sale.setTotalAmount(quantity * ProductService.getProductById(selectedProduct.getId()).getPrice());
                cartList.add(sale);
                updateTotal();
                clearFields();
            } else {
                showAlert("Error", "Failed to add item to sale! Check stock availability.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void confirmSale() {
        if (cartList.isEmpty()) {
            showAlert("Warning", "No items in cart to confirm!", Alert.AlertType.WARNING);
            return;
        }
        if (SaleService.confirmSaleGroup(saleGroupId)) {
            showAlert("Success", "Sale confirmed successfully!", Alert.AlertType.INFORMATION);
            if (saleController != null) saleController.loadSaleData();
            closeWindow();
        } else {
            showAlert("Error", "Failed to confirm sale!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateSale() {
        if (currentSale == null) {
            showAlert("Warning", "No sale item to update!", Alert.AlertType.WARNING);
            return;
        }
        if (validateQuantity()) {
            int newQuantity = Integer.parseInt(quantityField.getText());
            if (SaleService.updateSaleItem(currentSale.getId(), newQuantity)) {
                showAlert("Success", "Sale item updated successfully!", Alert.AlertType.INFORMATION);
                if (saleController != null) saleController.loadSaleData();
                closeWindow();
            } else {
                showAlert("Error", "Failed to update sale item! Check stock availability.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private boolean validateInputs() {
        if (productCombo.getValue() == null || quantityField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Product and Quantity are required!", Alert.AlertType.WARNING);
            return false;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be positive!", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Quantity must be a valid number!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateQuantity() {
        if (quantityField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Quantity is required!", Alert.AlertType.WARNING);
            return false;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be positive!", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Quantity must be a valid number!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void clearFields() {
        productCombo.setValue(null);
        quantityField.clear();
    }

    private void updateTotal() {
        double total = cartList.stream().mapToDouble(Sale::getTotalAmount).sum();
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    private void closeWindow() {
        Stage stage = (Stage) productCombo.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}