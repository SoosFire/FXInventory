package app.fxinventory;

import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Shop.Shop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("Main.fxml"));

        Scene scene = new Scene(loader.load(), 1000, 750);
        stage.setTitle("Legend of CodeCraft");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
