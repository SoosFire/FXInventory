package app.fxinventory.Controllers;

import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Main;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

// Controller for hovedmenuen (Main.fxml).
// Ansvar: modtage de fælles objekter (Inventory, Shop) og starte selve spillet.
public class Main_Controller {

    // Referencer til scene og stage (bruges hvis man manuelt vil skifte).
    private Parent root;
    private Stage stage;
    private Scene scene;

    // Fælles Inventory- og Shop-objekter, som kommer fra Main.java.
    private Inventory inventory;
    private Shop shop;

    // Sættes fra Main.java, så denne controller har adgang til spillerens inventory.
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // Sættes fra Main.java, så denne controller har adgang til shoppen.
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    // Event-handler for "Start Game"-knappen på hovedmenuen.
    // Skifter scene til Game_Home.fxml via SceneNavigator
    // og sender Inventory og Shop videre til Game_Home_Controller.
    @FXML
    protected void onStartGame(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Game_Home.fxml", (Game_Home_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }
}
