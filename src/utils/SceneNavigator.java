package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {
    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchTo(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneNavigator.class.getResource("/views/" + fxmlFileName)
            );

            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/styles/style.css").toExternalForm()
            );

            mainStage.setScene(scene);
            mainStage.show();

        } catch (Exception e) {
            System.out.println("Navigation Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}