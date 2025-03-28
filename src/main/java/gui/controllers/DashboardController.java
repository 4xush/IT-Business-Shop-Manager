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
        if (hostServices != null && developerButton != null) {
            developerButton.setDisable(false);
        }
    }

    @FXML
    public void initialize() {
        productButton.setText("Product\nManagement");
        saleButton.setText("Sale\nManagement");
        repairButton.setText("Repair\nManagement");
        rechargeButton.setText("Recharge\nManagement");
        reportButton.setText("Reports");
        developerButton.setText("View\nDeveloper\nPage");
        developerButton.setDisable(true);
        settingsButton.setText("Settings");
    }

    @FXML
    private void openProductView() {
        loadView("/ProductView.fxml", "Product Management");
    }

    @FXML
    private void openSaleView() {
        loadView("/SaleView.fxml", "Sale Management");
    }

    @FXML
    private void openRepairView() {
        loadView("/RepairView.fxml", "Repair Management");
    }

    @FXML
    private void openRechargeView() {
        loadView("/RechargeView.fxml", "Recharge Management");
    }

    @FXML
    private void openReportView() {
        loadView("/ReportView.fxml", "Reports");
    }

    @FXML
    private void openDeveloperPage() {
        if (hostServices != null) {
            hostServices.showDocument("https://github.com/4xush/ShopSync-Manager");
        } else {
            System.out.println("HostServices not initialized!");
        }
    }

    @FXML
    private void openSettingsView() {
        loadView("/SettingsView.fxml", "Settings");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane root = loader.load();
            
            // Pass HostServices to controllers that support it
            Object controller = loader.getController();
            if (controller instanceof ProductController) {
                ((ProductController) controller).setHostServices(hostServices);
            } else if (controller instanceof SaleController) {
                ((SaleController) controller).setHostServices(hostServices);
            } else if (controller instanceof RepairController) {
                ((RepairController) controller).setHostServices(hostServices);
            } else if (controller instanceof RechargeController) {
                ((RechargeController) controller).setHostServices(hostServices);
            } else if (controller instanceof ReportController) {
                ((ReportController) controller).setHostServices(hostServices);
            } else if (controller instanceof SettingsController) {
                ((SettingsController) controller).setHostServices(hostServices);
            }
            
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

    // Method to be called from other controllers when returning to dashboard
    public static void returnToDashboard(Stage currentStage, HostServices hostServices) {
        try {
            FXMLLoader loader = new FXMLLoader(DashboardController.class.getResource("/Dashboard.fxml"));
            AnchorPane root = loader.load();
            DashboardController controller = loader.getController();
            controller.setHostServices(hostServices);
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("ShopSync Manager Dashboard");
            currentStage.setMaximized(true);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}