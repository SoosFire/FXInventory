package app.fxinventory.Controllers;

import app.fxinventory.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public final class SceneNavigator {

    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 750;

    private SceneNavigator() { }

        public static void switchTo(ActionEvent event, String fxmlName) throws IOException {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(Main.class.getResource(fxmlName))
            );
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();
            Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            stage.setScene(scene);
            stage.show();
        }

    public static <T> void switchTo(
            ActionEvent event,
            String fxmlName,
            Consumer<T> controllerInitializer) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(Main.class.getResource(fxmlName)));
        Parent root = loader.load();

        T controller = loader.getController();
        controllerInitializer.accept(controller);

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
