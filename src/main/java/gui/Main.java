package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import gui.controllers.DashboardController;
import database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting ShopSync Manager application");
        
        try {
            // Initialize database first
            DatabaseConnection.connect();
            DatabaseConnection.createTables();
            
            // Proceed with GUI setup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            AnchorPane root = loader.load();
            DashboardController controller = loader.getController();
            controller.setHostServices(getHostServices());
            
            Scene scene = new Scene(root);
            primaryStage.setTitle("ShopSync Manager Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
            
            logger.info("Application started successfully");
            
        } catch (SQLException e) {
            logger.error("Database initialization failed", e);
            showErrorAndExit("Database Error", 
                "Failed to initialize database:\n" + e.getMessage());
        } catch (Exception e) {
            logger.error("Application startup failed", e);
            showErrorAndExit("Fatal Error", 
                "Application failed to initialize:\n" + e.getMessage());
        }
    }

    private void showErrorAndExit(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            Platform.exit();
            System.exit(1);
        });
    }

    public static void main(String[] args) {
        logger.info("Launching ShopSync Manager application");
        launch(args);
    }
}