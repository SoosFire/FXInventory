package app.fxinventory.Controllers;

import app.fxinventory.Enums.ItemName;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class Game_Home_Controller {

    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    private Label gold_Label;
    @FXML
    private Label weight_Label;
    @FXML
    private TextArea inventory_TextArea;

    private Inventory inventory;
    private Shop shop;

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        updateHud();
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }


    @FXML
    public void giveGold(){
        inventory.addGold(50);
        gold_Label.setText(String.valueOf(inventory.getGold()));
    }

    @FXML
    public void showShopScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Shop.fxml", (Shop_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    @FXML
    public void showInventoryScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Inventory.fxml", (Inventory_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    @FXML
    public void showUpgradeScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Upgrades.fxml", (Upgrades_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    private void updateHud() {
        if (gold_Label != null) {
            gold_Label.setText(String.valueOf(inventory.getGold()));
        }
        if (weight_Label != null) {
            double w = inventory.getWeight();
            if (Math.abs(w) < 1e-3) {
                w = 0.0;
            }
            weight_Label.setText(
                    String.format("%.1f/%d", w, inventory.getWeightLimit())
            );
        }
    }


}
