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

    @FXML
    public void giveGold(){
        inventory.addGold(50);
        gold_Label.setText(String.valueOf(inventory.getGold()));
    }

    @FXML
    public void showShopScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Shop.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000,750);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void showInventoryScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Inventory.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000,750);
        stage.setScene(scene);
        stage.show();
    }


}
