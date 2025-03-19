package gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Recharge;
import services.RechargeService;

public class RechargeFormController {

    @FXML private TextField mobileField;
    @FXML private TextField operatorField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> statusCombo; // Changed to ComboBox
    @FXML private Button addButton;
    @FXML private Button updateButton;

    private RechargeController rechargeController;
    private Recharge currentRecharge;
    private RechargeService rechargeService = new RechargeService();

    @FXML
    public void initialize() {
        // Populate ComboBox with valid statuses
        statusCombo.setItems(FXCollections.observableArrayList("Pending", "Successful", "Failed"));
    }

    public void setRecharge(Recharge recharge, RechargeController controller) {
        this.rechargeController = controller;
        this.currentRecharge = recharge;
        if (recharge != null) {
            mobileField.setText(recharge.getCustomerMobile());
            operatorField.setText(recharge.getOperator());
            amountField.setText(String.valueOf(recharge.getAmount()));
            statusCombo.setValue(recharge.getStatus()); // Set current status
            mobileField.setDisable(true);
            operatorField.setDisable(true);
            amountField.setDisable(true);
            addButton.setDisable(true);
            updateButton.setDisable(false);
        } else {
            clearFields();
            mobileField.setDisable(false);
            operatorField.setDisable(false);
            amountField.setDisable(false);
            addButton.setDisable(false);
            updateButton.setDisable(true);
        }
    }

    @FXML
    private void addRecharge() {
        if (validateInputs()) {
            String mobile = mobileField.getText();
            String operator = operatorField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String status = statusCombo.getValue(); // Get from ComboBox
            Recharge recharge = new Recharge(mobile, operator, amount);
            recharge.setStatus(status != null ? status : "Pending"); // Default to Pending if null
            if (rechargeService.addRecharge(recharge)) {
                showAlert("Success", "Recharge added successfully!", Alert.AlertType.INFORMATION);
                if (rechargeController != null) rechargeController.loadRechargeData();
                closeWindow();
            } else {
                showAlert("Error", "Failed to add recharge!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateRecharge() {
        if (currentRecharge == null) {
            showAlert("Warning", "No recharge to update!", Alert.AlertType.WARNING);
            return;
        }
        String status = statusCombo.getValue();
        if (status == null) {
            showAlert("Validation Error", "Status is required!", Alert.AlertType.WARNING);
            return;
        }
        if (rechargeService.updateRechargeStatus(currentRecharge.getId(), status)) {
            showAlert("Success", "Recharge status updated successfully!", Alert.AlertType.INFORMATION);
            if (rechargeController != null) rechargeController.loadRechargeData();
            closeWindow();
        } else {
            showAlert("Error", "Failed to update recharge status!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private boolean validateInputs() {
        if (mobileField.getText().trim().isEmpty() ||
            operatorField.getText().trim().isEmpty() ||
            amountField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Mobile, Operator, and Amount are required!", Alert.AlertType.WARNING);
            return false;
        }
        try {
            Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Amount must be a valid number!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void clearFields() {
        mobileField.clear();
        operatorField.clear();
        amountField.clear();
        statusCombo.setValue(null);
    }

    private void closeWindow() {
        Stage stage = (Stage) mobileField.getScene().getWindow();
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