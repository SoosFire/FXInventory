package app.fxinventory.Controllers;

import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Item.Item;
import app.fxinventory.Main;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Inventory_Controller {

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
    private VBox inventory_VBox;

    @FXML
    public void initialize() {
        updateHud();
        rebuildInventoryVBox();
    }


    private void updateHud() {
        gold_Label.setText(String.valueOf(inventory.getGold()));
        weight_Label.setText(inventory.getWeight() + "/50");
    }

    private void rebuildInventoryVBox() {
        inventory_VBox.getChildren().clear();

        for (Item item : inventory.getItems()) {
            Label label = new Label(item.getTotalAmount() + "x " + item.getName());
            Button button = new Button("Sell");

            button.setOnAction(e -> {
                shop.sellItem(inventory, item);
                updateHud();
                rebuildInventoryVBox();
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(50);
            row.getChildren().addAll(label, spacer, button);

            // Make the row stretch to full width of the VBox
            row.setMaxWidth(Double.MAX_VALUE);

            inventory_VBox.getChildren().add(row);
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
