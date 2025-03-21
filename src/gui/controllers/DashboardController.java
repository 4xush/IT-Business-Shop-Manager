package gui.controllers;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {

    @FXML private AnchorPane dashboardPane;
    @FXML private Button productButton;
    @FXML private Button saleButton;
    @FXML private Button repairButton;
    @FXML private Button rechargeButton;
    @FXML private Button reportButton;
    @FXML private Button developerButton;
    @FXML private Button settingsButton;

    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        productButton.setText("Product\nManagement");
        saleButton.setText("Sale\nManagement");
        repairButton.setText("Repair\nManagement");
        rechargeButton.setText("Recharge\nManagement");
        reportButton.setText("Reports");
        developerButton.setText("View\nDeveloper\nPage");
        settingsButton.setText("Settings");
    }

    @FXML
    private void openProductView() {
        loadView("../views/ProductView.fxml", "Product Management");
    }

    @FXML
    private void openSaleView() {
        loadView("../views/SaleView.fxml", "Sale Management");
    }

    @FXML
    private void openRepairView() {
        loadView("../views/RepairView.fxml", "Repair Management");
    }

    @FXML
    private void openRechargeView() {
        loadView("../views/RechargeView.fxml", "Recharge Management");
    }

    @FXML
    private void openReportView() {
        loadView("../views/ReportView.fxml", "Report Management");
    }

    @FXML
    private void openDeveloperPage() {
        if (hostServices == null) {
            System.out.println("HostServices not initialized yet! Retrying after setup...");
            // Fallback: Could show an alert or wait for initialization
            javafx.application.Platform.runLater(() -> {
                if (hostServices != null) {
                    hostServices.showDocument("https://github.com/4xush/ShopSync-Manager");
                } else {
                    System.out.println("HostServices still null after retry!");
                }
            });
        } else {
            hostServices.showDocument("https://github.com/4xush/ShopSync-Manager");
        }
    }

    @FXML
    private void openSettingsView() {
        loadView("../views/SettingsView.fxml", "Settings");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane root = loader.load();
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}