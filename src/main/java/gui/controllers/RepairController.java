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
import models.Repair;
import services.RepairService;
import java.util.List;

public class RepairController {

    @FXML private TableView<Repair> repairTable;
    @FXML private TableColumn<Repair, Integer> colId;
    @FXML private TableColumn<Repair, String> colCustomerName;
    @FXML private TableColumn<Repair, String> colPhoneNumber;
    @FXML private TableColumn<Repair, String> colDeviceModel;
    @FXML private TableColumn<Repair, String> colIssueDescription;
    @FXML private TableColumn<Repair, Double> colEstimatedCost;
    @FXML private TableColumn<Repair, String> colStatus;
    @FXML private TableColumn<Repair, java.sql.Timestamp> colCreatedAt;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private ObservableList<Repair> repairList = FXCollections.observableArrayList();
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colDeviceModel.setCellValueFactory(new PropertyValueFactory<>("deviceModel"));
        colIssueDescription.setCellValueFactory(new PropertyValueFactory<>("issueDescription"));
        colEstimatedCost.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        
        loadRepairData();

        repairTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                boolean isCompleted = "Completed".equals(newSelection.getStatus());
                updateButton.setDisable(isCompleted);
                deleteButton.setDisable(isCompleted);
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
    }

    public void loadRepairData() {
        List<Repair> repairs = RepairService.getAllRepairs();
        repairList.setAll(repairs);
        repairTable.setItems(repairList);
    }

    @FXML
    private void addRepair() {
        openRepairForm(null);
    }

    @FXML
    private void updateRepair() {
        Repair selected = repairTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Repair Selected", "Please select a repair to update.", Alert.AlertType.WARNING);
            return;
        }
        if ("Completed".equals(selected.getStatus())) {
            showAlert("Action Not Allowed", "Cannot update a completed repair.", Alert.AlertType.WARNING);
            return;
        }
        openRepairForm(selected);
    }

    @FXML
    private void deleteRepair() {
        Repair selected = repairTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Repair Selected", "Please select a repair to delete.", Alert.AlertType.WARNING);
            return;
        }
        if ("Completed".equals(selected.getStatus())) {
            showAlert("Action Not Allowed", "Cannot delete a completed repair.", Alert.AlertType.WARNING);
            return;
        }
        if (RepairService.deleteRepair(selected.getId())) {
            loadRepairData();
            showAlert("Success", "Repair deleted successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to delete repair!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void backToDashboard() {
        try {
            Stage stage = (Stage) repairTable.getScene().getWindow();
            DashboardController.returnToDashboard(stage, hostServices);
        } catch (Exception e) {
            showAlert("Error", "Failed to return to dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void openRepairForm(Repair repair) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RepairForm.fxml"));
            AnchorPane root = loader.load();
            RepairFormController controller = loader.getController();
            controller.setRepair(repair, this);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Set window properties
            stage.setTitle(repair == null ? "Add Repair" : "Update Repair");
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            
            // Center on screen
            stage.centerOnScreen();
            
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to open repair form: " + e.getMessage(), Alert.AlertType.ERROR);
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