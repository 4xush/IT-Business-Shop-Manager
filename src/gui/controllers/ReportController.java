package gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Report;
import services.ReportService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ReportController {

    // FXML Elements
    @FXML private ComboBox<String> periodComboBox;
    @FXML private Label totalSalesLabel;
    @FXML private Label totalSalesAmountLabel;
    @FXML private Label totalRepairsLabel;
    @FXML private Label totalRepairRevenueLabel;
    @FXML private Label totalRechargesLabel;
    @FXML private Label totalRechargeAmountLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label reportPeriodLabel;
    @FXML private Button backButton;

    private final ReportService reportService = new ReportService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    @FXML
    public void initialize() {
        periodComboBox.setItems(FXCollections.observableArrayList("Daily", "Weekly", "Monthly"));
        periodComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void generateReport() {
        String period = periodComboBox.getValue();
        if (period == null) {
            showAlert("Selection Required", "Please choose a report period.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Report report = reportService.generateReport(period.toLowerCase());
            updateReportDisplay(report);
        } catch (Exception e) {
            showAlert("Report Error", "Failed to generate report: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void updateReportDisplay(Report report) {
        // Sales Section
        totalSalesLabel.setText(String.format("%,d", report.getTotalSales()));
        totalSalesAmountLabel.setText(String.format("₹%,.2f", report.getTotalSalesAmount()));

        // Repairs Section
        totalRepairsLabel.setText(String.format("%,d", report.getTotalRepairsCompleted()));
        totalRepairRevenueLabel.setText(String.format("₹%,.2f", report.getTotalRepairRevenue()));

        // Recharges Section
        totalRechargesLabel.setText(String.format("%,d", report.getTotalRechargesCompleted()));
        totalRechargeAmountLabel.setText(String.format("₹%,.2f", report.getTotalRechargeAmount()));

        // Inventory Section
        lowStockLabel.setText(String.format("%,d items", report.getLowStockProducts()));

        // Report Period
        String periodRange = String.format("%s - %s",
                report.getStartTime().format(dateFormatter),
                report.getEndTime().format(dateFormatter));
        reportPeriodLabel.setText(periodRange);
    }

    @FXML
    private void backToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Dashboard.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("IT Service Shop Dashboard");
            stage.setMaximized(true);
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
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