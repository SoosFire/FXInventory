package app.fxinventory.Controllers;

import app.fxinventory.Enums.ItemType;
import app.fxinventory.Enums.WeaponSlotType;
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
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    private ArrayList<Item> displayedItems = new ArrayList<>();

    @FXML
    private VBox container;
    @FXML

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
    MenuButton weaponFilter_MenuButton = new  MenuButton("Weapon Filter");

    @FXML
    MenuButton itemTypeFilter_MenuButton = new  MenuButton("Weapon Filter");

    private MenuItem filter_one_handed = new MenuItem("One-Handed");
    private MenuItem filter_two_handed = new MenuItem("Two-Handed");
    private MenuItem filter_dual_handed = new MenuItem("Dual-Handed");
    private MenuItem filter_ItemType_Weapon = new MenuItem("Weapon");
    private MenuItem filter_ItemType_Armor = new MenuItem("Armor");
    private MenuItem filter_ItemType_Utility = new MenuItem("Utility");
    private MenuItem filter_ItemType_Consumable = new MenuItem("Consumable");

    @FXML
    public void initialize() {
        updateHud();
        buildInventory(0,startDisplayInventory());
        menuButtonFilter();
    }

    private void buildInventory(int index, ArrayList<Item> displayedItems) {
        inventoryVBox.getChildren().clear();

        for (int i = 0; i < ROWS; i++) {
            HBox row = new HBox(7);
            row.setAlignment(Pos.CENTER_LEFT);

            for (int j = 0; j < COLS; j++) {
                Item item = index < displayedItems.size() ? displayedItems.get(index) : null;
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
        displayedItems.clear();
        startDisplayInventory();
        buildInventory(0,displayedItems);
    }

    ///////////////////////////////////////////////////////////

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


        pageLabel.setText(inventoryPage + "/" + 8);
    }

    ////////////////////////////////////////////////////////

    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Game_Home.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000,750);
        stage.setScene(scene);
        stage.show();
    }

    ////////////////////////////////////////////////////////

    @FXML
    public void onClickNextPage() {
        inventoryPage++;
        index = (inventoryPage - 1) * 24;
        buildInventory(index,displayedItems);
        updateHud();
    }
    @FXML
    public void onClickPreviousPage() {
        if (inventoryPage > 1) {
            inventoryPage--;
        }
        index = (inventoryPage - 1) * 24;
        buildInventory(index,displayedItems);
        updateHud();
    }


    ////////////////////////////////////////////////////////
    // Item Filter -> ItemType [Weapon]
    public void onFilterWeaponClick (ItemType itemType) {
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item.getType() == itemType) {
                displayedItems.add(item);
            }
        }
        buildInventory(0,displayedItems);
    }

    // Item Filter -> ItemType [Armor]
    public void onFilterArmorClick (ItemType itemType) {
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item.getType() == ItemType.ARMOR){
                displayedItems.add(item);
            }
        }
        buildInventory(0,displayedItems);
    }

    // Item Filter -> ItemType [Utility]
    public void onFilterUtilityClick (ItemType itemType){
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item.getType() == ItemType.UTILITY){
                displayedItems.add(item);
            }
        }
        buildInventory(0,displayedItems);
    }

    // Item Filter -> ItemType [Consumable]
    public void onFilterConsumableClick (ItemType itemType) {
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item.getType() == itemType){
                displayedItems.add(item);
            }
        }
        buildInventory(0,displayedItems);
    }

    // Weapon Filter -> ItemType [ONE_HANDED]
    public void onFilterOneHanded (WeaponSlotType slotType) {
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item instanceof Weapon weapon)
                if (weapon.getSlotType() == slotType) {
                displayedItems.add(item);
            }
        }
        buildInventory(0,displayedItems);
    }

    // Weapon Filter -> ItemType [TWO_HANDED]
    public void onFilterTwoHanded (WeaponSlotType slotType) {
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item instanceof Weapon weapon)
                if (weapon.getSlotType() == slotType) {
                    displayedItems.add(item);
                }
        }
        buildInventory(0,displayedItems);
    }

    // Weapon Filter -> ItemType [DUAL_HANDED]
    public void onFilterDualHanded (WeaponSlotType slotType) {
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item instanceof Weapon weapon)
                if (weapon.getSlotType() == slotType) {
                    displayedItems.add(item);
                }
        }
        buildInventory(0,displayedItems);
    }


    // Start items af ArrayList<Item> displayedItems
    // Viser her hele inventory
    private ArrayList<Item> startDisplayInventory () {
        displayedItems.clear();
        displayedItems.addAll(inventory.getItems());
        return displayedItems;
    }

    // Opretter Actions for dropdown buttons i hver menuButton.
    private void menuButtonFilter () {

        // WeaponFilter_MenuButton -> Filterer for WeaponSlotType
        // Sætter OnAction for hver dropdown-menu (MenuItem)
        weaponFilter_MenuButton.getItems().clear();
        weaponFilter_MenuButton.setText("Weapon Filter");
        filter_one_handed.setOnAction(e -> onFilterOneHanded(WeaponSlotType.ONE_HANDED));           // -> Metode onFilterOneHanded(WeaponSlotType.ONE_HANDED)
        filter_two_handed.setOnAction(e -> onFilterTwoHanded(WeaponSlotType.TWO_HANDED));           // -> Metode onFilterTwoHanded(WeaponSlotType.TWO_HANDED)
        filter_dual_handed.setOnAction(e -> onFilterDualHanded(WeaponSlotType.DUAL_HANDED));        // -> Metode onFilterDualHanded(WeaponSlotType.DUAL_HANDED)
        weaponFilter_MenuButton.getItems().addAll(filter_one_handed, filter_two_handed, filter_dual_handed);   // -> Tilføjer alle MenuItems til MenuButton.

        itemTypeFilter_MenuButton.getItems().clear();
        itemTypeFilter_MenuButton.setText("Item Type");
        filter_ItemType_Weapon.setOnAction(e -> onFilterWeaponClick(ItemType.WEAPON));
        filter_ItemType_Armor.setOnAction(e -> onFilterArmorClick(ItemType.ARMOR));
        filter_ItemType_Utility.setOnAction(e -> onFilterUtilityClick(ItemType.UTILITY));
        filter_ItemType_Consumable.setOnAction(e -> onFilterConsumableClick(ItemType.CONSUMABLE));
        itemTypeFilter_MenuButton.getItems().addAll(filter_ItemType_Weapon, filter_ItemType_Armor, filter_ItemType_Utility, filter_ItemType_Consumable);
    }
}
