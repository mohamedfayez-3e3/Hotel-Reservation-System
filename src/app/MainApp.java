package app;

import database.HotelDatabase;
import javafx.application.Application;
import javafx.stage.Stage;
import utils.SceneNavigator;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        HotelDatabase.initializeData();

        SceneNavigator.setMainStage(primaryStage);

        primaryStage.setTitle("Hotel Reservation System");
        primaryStage.setResizable(false);

        SceneNavigator.switchTo("login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}