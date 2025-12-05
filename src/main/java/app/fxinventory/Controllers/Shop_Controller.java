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

public class Shop_Controller {

    private Parent root;
    private Scene scene;
    private Stage stage;

    private final Inventory inventory = Main.getInventory();
    private final Shop shop = Main.getShop();

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
    public void onBackButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Game_Home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000,750);
        stage.setScene(scene);
        stage.show();
    }
}
