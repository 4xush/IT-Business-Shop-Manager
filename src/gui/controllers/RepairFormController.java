package gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Repair;
import services.RepairService;

public class RepairFormController {

    @FXML private TextField customerNameField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField deviceModelField;
    @FXML private TextField issueDescriptionField;
    @FXML private TextField estimatedCostField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button addButton;
    @FXML private Button updateButton;

    private RepairController repairController;
    private Repair currentRepair;

    @FXML
    public void initialize() {
        statusCombo.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Completed"));
    }

    public void setRepair(Repair repair, RepairController controller) {
        this.repairController = controller;
        this.currentRepair = repair;
        if (repair != null) {
            customerNameField.setText(repair.getCustomerName());
            phoneNumberField.setText(repair.getPhoneNumber());
            deviceModelField.setText(repair.getDeviceModel());
            issueDescriptionField.setText(repair.getIssueDescription());
            estimatedCostField.setText(String.valueOf(repair.getEstimatedCost()));
            statusCombo.setValue(repair.getStatus());
            customerNameField.setDisable(true);
            phoneNumberField.setDisable(true);
            deviceModelField.setDisable(true);
            issueDescriptionField.setDisable(true);
            estimatedCostField.setDisable(true);
            addButton.setDisable(true);
            updateButton.setDisable(false);
        } else {
            clearFields();
            customerNameField.setDisable(false);
            phoneNumberField.setDisable(false);
            deviceModelField.setDisable(false);
            issueDescriptionField.setDisable(false);
            estimatedCostField.setDisable(false);
            addButton.setDisable(false);
            updateButton.setDisable(true);
        }
    }

    @FXML
    private void addRepair() {
        if (validateInputs()) {
            String customerName = customerNameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String deviceModel = deviceModelField.getText();
            String issueDescription = issueDescriptionField.getText();
            double estimatedCost = Double.parseDouble(estimatedCostField.getText());
            if (RepairService.addRepair(customerName, phoneNumber, deviceModel, issueDescription, estimatedCost)) {
                showAlert("Success", "Repair added successfully!", Alert.AlertType.INFORMATION);
                if (repairController != null) repairController.loadRepairData();
                closeWindow();
            } else {
                showAlert("Error", "Failed to add repair!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateRepair() {
        if (currentRepair == null) {
            showAlert("Warning", "No repair to update!", Alert.AlertType.WARNING);
            return;
        }
        String status = statusCombo.getValue();
        if (status == null) {
            showAlert("Validation Error", "Status is required!", Alert.AlertType.WARNING);
            return;
        }
        if (RepairService.updateRepairStatus(currentRepair.getId(), status)) {
            showAlert("Success", "Repair status updated successfully!", Alert.AlertType.INFORMATION);
            if (repairController != null) repairController.loadRepairData();
            closeWindow();
        } else {
            showAlert("Error", "Failed to update repair status!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private boolean validateInputs() {
        if (customerNameField.getText().trim().isEmpty() ||
            phoneNumberField.getText().trim().isEmpty() ||
            deviceModelField.getText().trim().isEmpty() ||
            issueDescriptionField.getText().trim().isEmpty() ||
            estimatedCostField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.WARNING);
            return false;
        }
        try {
            Double.parseDouble(estimatedCostField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Estimated Cost must be a valid number!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void clearFields() {
        customerNameField.clear();
        phoneNumberField.clear();
        deviceModelField.clear();
        issueDescriptionField.clear();
        estimatedCostField.clear();
        statusCombo.setValue(null);
    }

    private void closeWindow() {
        Stage stage = (Stage) customerNameField.getScene().getWindow();
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