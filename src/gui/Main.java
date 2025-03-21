package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import gui.controllers.DashboardController;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/Dashboard.fxml"));
        AnchorPane root = loader.load();
        DashboardController controller = loader.getController();
        controller.setHostServices(getHostServices());
        Scene scene = new Scene(root);
        primaryStage.setTitle("ShopSync Manager Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}