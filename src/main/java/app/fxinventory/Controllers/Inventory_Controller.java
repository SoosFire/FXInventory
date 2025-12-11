package app.fxinventory.Controllers;

import app.fxinventory.Enums.ItemType;
import app.fxinventory.Item.*;

import app.fxinventory.Enums.ItemName;
import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Main;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inventory_Controller {

    private final Inventory inventory = Main.getInventory();
    private final Shop shop = Main.getShop();

    // 3 rows x 6 columns = 18 slots total
    private static final int ROWS = 3;
    private static final int COLS = 8;
    private  int index = 0;
    private  int inventoryPage = 1;

    private static final double SLOT_IMAGE_SIZE = 100;
    private static final double SLOT_BUTTON_WIDTH = 100;
    private static final double SLOT_BUTTON_HEIGHT = 50;

    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    private VBox inventoryVBox;

    @FXML
    private Label gold_Label;

    @FXML
    private Label weight_Label;

    @FXML
    private Label pageLabel;

    @FXML
    public void initialize() {
        updateHud();
        buildInventory(0);
    }

    private void buildInventory(int index) {
        inventoryVBox.getChildren().clear();

        List<Item> items = inventory.getItems();

        for (int i = 0; i < ROWS; i++) {
            HBox row = new HBox(7);
            row.setAlignment(Pos.CENTER_LEFT);

            for (int j = 0; j < COLS; j++) {
                Item item = index < items.size() ? items.get(index) : null;
                VBox slot = createSlotCard(item);
                row.getChildren().add(slot);
                index++;
            }

            inventoryVBox.getChildren().add(row);
        }
    }

    private VBox createSlotCard(Item item) {
        // Outer card for a single slot
        VBox card = new VBox(3);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(SLOT_IMAGE_SIZE);
        card.setMinWidth(SLOT_IMAGE_SIZE);
        card.setMaxWidth(SLOT_IMAGE_SIZE); // tiny bit of margi
        card.getStyleClass().add("name_card");

        // Image + amount overlay
        StackPane imageStack = new StackPane();
        imageStack.setPrefSize(SLOT_IMAGE_SIZE, SLOT_IMAGE_SIZE);
        imageStack.setMinSize(SLOT_IMAGE_SIZE, SLOT_IMAGE_SIZE);
        imageStack.setMaxSize(SLOT_IMAGE_SIZE, SLOT_IMAGE_SIZE);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(SLOT_IMAGE_SIZE);
        imageView.setFitHeight(SLOT_IMAGE_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        Label amountLabel = new Label();
        amountLabel.setTextFill(Color.WHITE);

        if (item != null) {
            // Adjust this if your Item class exposes the enum differently
            // e.g. item.getItemName()
            ItemName itemName = ItemNameToItemNameRegistry.fromDisplayName(item.getName());
            String path = ItemImageRegistry.getDefinition(itemName);
            if (path != null) {
                URL url = getClass().getResource(path);
                if (url != null) {
                    imageView.setImage(new Image(url.toExternalForm()));
                }
            }
            if (item instanceof Utility utility){
                amountLabel.setText(String.valueOf(utility.getStackSize()));
            }
            else if (item instanceof Consumable consumable){
                amountLabel.setText(String.valueOf(consumable.getStackSize()));
            } else if (item instanceof Weapon || item instanceof Armor){
                amountLabel.setText("1");
            }

        } else {
            // Optional: empty slot placeholder
            URL url = getClass().getResource("/app/fxinventory/Images/empty_slot.png");
            if (url != null) {
                imageView.setImage(new Image(url.toExternalForm()));
            }
            amountLabel.setText("");
        }

        StackPane.setAlignment(amountLabel, Pos.BOTTOM_LEFT);
        StackPane.setMargin(amountLabel, new Insets(0, 0, 3, 3));

        imageStack.getChildren().addAll(imageView, amountLabel);

        // Button under the image
        Button slotButton = new Button(item != null ? "SELL" : "");
        slotButton.setPrefWidth(SLOT_BUTTON_WIDTH);
        slotButton.setMinWidth(SLOT_BUTTON_WIDTH);
        slotButton.setMaxWidth(SLOT_BUTTON_WIDTH);
        slotButton.setPrefHeight(SLOT_BUTTON_HEIGHT);

        if (item == null) {
            slotButton.setDisable(true);
        } else {
            slotButton.setOnAction(e -> onSlotSellClicked(item));
        }

        VBox.setVgrow(imageStack, Priority.NEVER);
        VBox.setVgrow(slotButton, Priority.NEVER);

        card.getChildren().addAll(imageStack, slotButton);
        return card;
    }

    // TO DO
    @FXML
    public void onSlotSellClicked(Item item) {
        shop.sellItem(inventory, item);
        updateHud();
        buildInventory(0);
    }

    private void updateHud() {
        if (gold_Label != null) {
            gold_Label.setText(String.valueOf(inventory.getGold()));
        }
        if (weight_Label != null) {
            weight_Label.setText(inventory.getWeight() + "/" +  inventory.getWeightLimit());
        }

        pageLabel.setText(inventoryPage + "/" + 8);
    }

    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Game_Home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000,750);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onClickNextPage() {
        inventoryPage++;
        index = (inventoryPage - 1) * 24;
        buildInventory(index);
        updateHud();
    }
    @FXML
    public void onClickPreviousPage() {
        if (inventoryPage > 1) {
            inventoryPage--;
        }
        index = (inventoryPage - 1) * 24;
        buildInventory(index);
        updateHud();
    }

    @FXML
    public void onFilterWeaponClick (){
        inventoryVBox.getChildren().clear();

        index = 0;
        ArrayList<Item> weapons = new ArrayList<>();

        for (Item item : inventory.getItems()) {
            if (item.getType() == ItemType.WEAPON){
                weapons.add(item);
            }
        }

        for (int i = 0; i < ROWS; i++) {
            HBox row = new HBox(7);
            row.setAlignment(Pos.CENTER_LEFT);

            for (int j = 0; j < COLS; j++) {
                Item weapon = index < weapons.size() ? weapons.get(index) : null;
                VBox slot = createSlotCard(weapon);
                row.getChildren().add(slot);
                index++;
            }

            inventoryVBox.getChildren().add(row);
        }
    }
    @FXML
    public void onFilterArmorClick (){

    }
    @FXML
    public void onFilterUtilityClick (){

    }
    @FXML
    public void onFilterConsumableClick (){

    }

}
