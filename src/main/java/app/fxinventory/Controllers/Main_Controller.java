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

public class Main_Controller {

    private Parent root;
    private Stage stage;
    private Scene scene;

    private Inventory inventory;
    private Shop shop;

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @FXML
    protected void onStartGame(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Game_Home.fxml", (Game_Home_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }
}
