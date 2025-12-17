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

// Hovedklassen for JavaFX-applikationen.
// Arver fra Application og er entry point for GUI-delen.
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Opretter de fælles objekter, som skal bruges på tværs af scener:
        // ét Inventory-objekt til spillerens items og stats...
        Inventory inventory = new Inventory();
        // ... og ét Shop-objekt til shop-logikken.
        Shop shop = new Shop();

        // Loader den første skærm (Main-menuen) fra Main.fxml.
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Parent root = loader.load();

        // Henter controlleren, der hører til Main.fxml,
        // og giver den adgang til Inventory og Shop (dependency injection).
        Main_Controller controller = loader.getController();
        controller.setInventory(inventory);
        controller.setShop(shop);

        // Opretter scenen med root-node og en fast størrelse,
        // og viser den i det primære Stage (vindue).
        Scene scene = new Scene(root, 1000, 750);
        stage.setScene(scene);
        stage.show();
    }

}
