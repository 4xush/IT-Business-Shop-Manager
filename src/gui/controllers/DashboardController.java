package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DashboardController {

    @FXML private Button productButton;
    @FXML private Button saleButton;
    @FXML private Button repairButton;
    @FXML private Button rechargeButton;
    @FXML private Button reportButton;

    @FXML
    private void openProductView() {
        System.out.println("Opening Product Management View...");
    }

    @FXML
    private void openSaleView() {
        System.out.println("Opening Sales Management View...");
    }

    @FXML
    private void openRepairView() {
        System.out.println("Opening Repair Management View...");
    }

    @FXML
    private void openRechargeView() {
        System.out.println("Opening Recharge Management View...");
    }

    @FXML
    private void openReportView() {
        System.out.println("Opening Reports View...");
    }
}
