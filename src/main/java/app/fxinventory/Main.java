package app.fxinventory;

import app.fxinventory.Controllers.Game_Home_Controller;
import app.fxinventory.Controllers.Main_Controller;
import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Shop.Shop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Create shared objects
        Inventory inventory = new Inventory();
        Shop shop = new Shop();

        // Load first screen
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Parent root = loader.load();

        // Inject dependencies
        Main_Controller controller = loader.getController();
        controller.setInventory(inventory);
        controller.setShop(shop);

        // Show stage
        Scene scene = new Scene(root, 1000, 750);
        stage.setScene(scene);
        stage.show();
    }

}
