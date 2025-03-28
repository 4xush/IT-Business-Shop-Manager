package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowUtils {
    
    public static void openFormWindow(String fxmlPath, String title, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            // Get the controller if it was passed
            if (controller != null) {
                Object formController = loader.getController();
                // You might need to add specific controller setup methods here
            }
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            
            // Set window properties
            stage.setScene(scene);
            stage.setTitle(title);
            
            // Get the preferred size from the root node
            double prefWidth = root.prefWidth(-1);
            double prefHeight = root.prefHeight(-1);
            
            // If FXML doesn't specify sizes, use default values
            stage.setWidth(prefWidth > 0 ? prefWidth : 800);
            stage.setHeight(prefHeight > 0 ? prefHeight : 600);
            
            // Set minimum size
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            
            // Center on screen
            stage.centerOnScreen();
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 