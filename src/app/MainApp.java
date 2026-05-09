package app;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;
import utils.SceneNavigator;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initializeDatabase();

        SceneNavigator.setMainStage(primaryStage);

        primaryStage.setTitle("Hotel Reservation System");
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> DatabaseManager.saveAllData());

        SceneNavigator.switchTo("login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}