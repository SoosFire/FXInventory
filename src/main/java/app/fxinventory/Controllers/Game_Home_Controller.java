package app.fxinventory.Controllers;

import app.fxinventory.Enums.ItemName;
import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Shop.Shop;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class Game_Home_Controller {

    Inventory inventory =  new Inventory();
    Shop shop = new Shop();

    @FXML
    private Label gold_Label;
    @FXML
    private Label weight_Label;
    @FXML
    private TextArea inventory_TextArea;

    @FXML
    public void initialize() {
        gold_Label.setText(String.valueOf(inventory.getGold()));
        weight_Label.setText(String.valueOf(inventory.getWeight()) + "/50");
    }

    @FXML
    public void giveGold(){
        inventory.addGold(50);
        gold_Label.setText(String.valueOf(inventory.getGold()));
    }

    @FXML
    public void buyItem(){
        if (inventory.getGold() >= 200){
            shop.buyItem(inventory, ItemName.Sword);

            inventory.addGold(-200);
            inventory.addWeight(4.1);

            weight_Label.setText(String.valueOf(inventory.getWeight()) + "/50");
            gold_Label.setText(String.valueOf(inventory.getGold()));
        }
    }

    @FXML
    public void showInventory(){
        inventory_TextArea.clear();
        inventory_TextArea.appendText(inventory.showInventory());
    }


}
