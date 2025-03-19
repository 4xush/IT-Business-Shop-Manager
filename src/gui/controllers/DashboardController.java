package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class DashboardController {

    // AnchorPane to anchor the method to the FXML (optional, if you need a reference point)
    @FXML
    private AnchorPane dashboardPane;

    public void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/" + fxmlFile));
            Parent root = loader.load();
            // Use the current stage from the dashboardPane or another node
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true); // Fullscreen mode
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openProductView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ProductView.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Product Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSaleView() {
        loadScene("SaleView.fxml");
    }

    @FXML
    private void openRepairView() {
        loadScene("RepairView.fxml");
    }

    @FXML
    private void openRechargeView() {
        loadScene("RechargeView.fxml");
    }

    @FXML
    private void openReportView() {
        loadScene("ReportView.fxml");
    }
}