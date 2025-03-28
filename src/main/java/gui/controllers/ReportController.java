package gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.HostServices;
import models.Report;
import services.ReportService;
import services.ReportService.SaleItem;
import services.ReportService.RepairItem;
import services.ReportService.RechargeItem;
import services.ReportService.ProductItem;
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
    private Report currentReport; // Store the current report for "Show" buttons
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

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
            currentReport = reportService.generateReport(period.toLowerCase());
            updateReportDisplay(currentReport);
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
    private void showSales() {
        if (currentReport == null || currentReport.getSalesItems().isEmpty()) {
            showAlert("Info", "No sales to display.", Alert.AlertType.INFORMATION);
            return;
        }
        StringBuilder details = new StringBuilder("Sales Details:\n");
        for (SaleItem item : currentReport.getSalesItems()) {
            details.append(String.format("ID: %d, Amount: ₹%,.2f, Time: %s\n",
                item.getSaleId(), item.getTotalAmount(), item.getSaleTime().format(dateFormatter)));
        }
        showAlert("Sales", details.toString(), Alert.AlertType.INFORMATION);
    }

    @FXML
    private void showRepairs() {
        if (currentReport == null || currentReport.getRepairItems().isEmpty()) {
            showAlert("Info", "No repairs to display.", Alert.AlertType.INFORMATION);
            return;
        }
        StringBuilder details = new StringBuilder("Repair Details:\n");
        for (RepairItem item : currentReport.getRepairItems()) {
            details.append(String.format("ID: %d, Cost: ₹%,.2f, Time: %s\n",
                item.getRepairId(), item.getEstimatedCost(), item.getCreatedAt().format(dateFormatter)));
        }
        showAlert("Repairs", details.toString(), Alert.AlertType.INFORMATION);
    }

    @FXML
    private void showRecharges() {
        if (currentReport == null || currentReport.getRechargeItems().isEmpty()) {
            showAlert("Info", "No recharges to display.", Alert.AlertType.INFORMATION);
            return;
        }
        StringBuilder details = new StringBuilder("Recharge Details:\n");
        for (RechargeItem item : currentReport.getRechargeItems()) {
            details.append(String.format("ID: %d, Amount: ₹%,.2f, Time: %s\n",
                item.getRechargeId(), item.getAmount(), item.getRequestTime().format(dateFormatter)));
        }
        showAlert("Recharges", details.toString(), Alert.AlertType.INFORMATION);
    }

    @FXML
    private void showLowStock() {
        if (currentReport == null || currentReport.getLowStockItems().isEmpty()) {
            showAlert("Info", "No low stock items to display.", Alert.AlertType.INFORMATION);
            return;
        }
        StringBuilder details = new StringBuilder("Low Stock Items:\n");
        for (ProductItem item : currentReport.getLowStockItems()) {
            details.append(String.format("ID: %d, Name: %s, Stock: %d\n",
                item.getProductId(), item.getName(), item.getStock()));
        }
        showAlert("Low Stock", details.toString(), Alert.AlertType.INFORMATION);
    }

    @FXML
    private void backToDashboard() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            DashboardController.returnToDashboard(stage, hostServices);
        } catch (Exception e) {
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