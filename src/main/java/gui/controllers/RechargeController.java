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
import models.Recharge;
import services.RechargeService;
import java.util.List;

public class RechargeController {

    @FXML private TableView<Recharge> rechargeTable;
    @FXML private TableColumn<Recharge, Integer> colId;
    @FXML private TableColumn<Recharge, String> colMobile;
    @FXML private TableColumn<Recharge, String> colOperator;
    @FXML private TableColumn<Recharge, Double> colAmount;
    @FXML private TableColumn<Recharge, String> colStatus;
    @FXML private TableColumn<Recharge, java.time.LocalDateTime> colRequestTime;

    private RechargeService rechargeService = new RechargeService();
    private ObservableList<Recharge> rechargeList = FXCollections.observableArrayList();
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("customerMobile"));
        colOperator.setCellValueFactory(new PropertyValueFactory<>("operator"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRequestTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));

        loadRechargeData();
    }

    public void loadRechargeData() {
        List<Recharge> recharges = rechargeService.getAllRecharges();
        rechargeList.setAll(recharges);
        rechargeTable.setItems(rechargeList);
    }

    @FXML
    private void addRecharge() {
        openRechargeForm(null);
    }

    @FXML
    private void updateRecharge() {
        Recharge selected = rechargeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openRechargeForm(selected);
        } else {
            showAlert("No Recharge Selected", "Please select a recharge to update.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void backToDashboard() {
        try {
            Stage stage = (Stage) rechargeTable.getScene().getWindow();
            DashboardController.returnToDashboard(stage, hostServices);
        } catch (Exception e) {
            showAlert("Error", "Failed to return to dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void openRechargeForm(Recharge recharge) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RechargeForm.fxml"));
            AnchorPane root = loader.load();
            RechargeFormController controller = loader.getController();
            controller.setRecharge(recharge, this);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            // Set window properties
            stage.setTitle(recharge == null ? "Add Recharge" : "Update Recharge");
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            
            // Center on screen
            stage.centerOnScreen();
            
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to open recharge form: " + e.getMessage(), Alert.AlertType.ERROR);
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