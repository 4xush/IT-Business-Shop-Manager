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
import javafx.application.HostServices;
import models.Sale;
import services.SaleService;

import java.util.List;

public class SaleController {

    @FXML private TableView<Sale> saleTable;
    @FXML private TableColumn<Sale, Integer> colId;
    @FXML private TableColumn<Sale, Integer> colSaleGroupId;
    @FXML private TableColumn<Sale, Integer> colProductId;
    @FXML private TableColumn<Sale, String> colProductName;
    @FXML private TableColumn<Sale, Integer> colQuantity;
    @FXML private TableColumn<Sale, Double> colTotalAmount;
    @FXML private TableColumn<Sale, String> colStatus;
    @FXML private TableColumn<Sale, java.sql.Timestamp> colSaleTime;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button confirmButton;

    private ObservableList<Sale> saleList = FXCollections.observableArrayList();
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSaleGroupId.setCellValueFactory(new PropertyValueFactory<>("saleGroupId"));
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSaleTime.setCellValueFactory(new PropertyValueFactory<>("saleTime"));
        loadSaleData();

        saleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                boolean isConfirmed = "Confirmed".equals(newSelection.getStatus());
                updateButton.setDisable(isConfirmed);
                deleteButton.setDisable(isConfirmed);
                confirmButton.setDisable(isConfirmed);
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                confirmButton.setDisable(true);
            }
        });
    }

    public void loadSaleData() {
        List<Sale> sales = SaleService.getAllSales();
        saleList.setAll(sales);
        saleTable.setItems(saleList);
    }

    @FXML
    private void addSale() {
        openSaleForm(null);
    }

    @FXML
    private void updateSale() {
        Sale selected = saleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Sale Selected", "Please select a sale item to update.", Alert.AlertType.WARNING);
            return;
        }
        if ("Confirmed".equals(selected.getStatus())) {
            showAlert("Action Not Allowed", "Cannot update a confirmed sale.", Alert.AlertType.WARNING);
            return;
        }
        openSaleForm(selected);
    }

    @FXML
    private void deleteSale() {
        Sale selected = saleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Sale Selected", "Please select a sale item to delete.", Alert.AlertType.WARNING);
            return;
        }
        if ("Confirmed".equals(selected.getStatus())) {
            showAlert("Action Not Allowed", "Cannot delete a confirmed sale.", Alert.AlertType.WARNING);
            return;
        }
        if (SaleService.deleteSaleItem(selected.getId())) {
            loadSaleData();
            showAlert("Success", "Sale item deleted successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to delete sale item!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void confirmSale() {
        Sale selected = saleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Sale Selected", "Please select a sale item to confirm its group.", Alert.AlertType.WARNING);
            return;
        }
        if ("Confirmed".equals(selected.getStatus())) {
            showAlert("Already Confirmed", "This sale group is already confirmed.", Alert.AlertType.WARNING);
            return;
        }
        if (SaleService.confirmSaleGroup(selected.getSaleGroupId())) {
            loadSaleData();
            showAlert("Success", "Sale group confirmed successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to confirm sale group!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void backToDashboard() {
        try {
            Stage stage = (Stage) saleTable.getScene().getWindow();
            DashboardController.returnToDashboard(stage, hostServices);
        } catch (Exception e) {
            showAlert("Error", "Failed to return to dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void openSaleForm(Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SaleForm.fxml"));
            AnchorPane root = loader.load();
            SaleFormController controller = loader.getController();
            controller.setSale(sale, this);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            // Set window properties
            stage.setTitle(sale == null ? "Add Sale" : "Update Sale Item");
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            
            // Center on screen
            stage.centerOnScreen();
            
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to open sale form: " + e.getMessage(), Alert.AlertType.ERROR);
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