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

// Controller for inventory-skærmen.
// Har ansvaret for at vise spillerens items, håndtere salg, filtrering og sideskift.
public class Inventory_Controller {

    // Reference til det fælles Inventory-objekt (spillerens data).
    private Inventory inventory;

    // Reference til det fælles Shop-objekt (bruges til salg af items).
    private Shop shop;

    // Antal rækker i inventory-griddet.
    private static final int ROWS = 3;

    // Antal kolonner i inventory-griddet.
    private static final int COLS = 8;

    // Index i displayedItems for det første item på den aktuelle side.
    private int index = 0;

    // Aktuelt sidenummer i inventory (1-baseret).
    private int inventoryPage = 1;

    // Billedstørrelse for item-ikoner (bredden/højden i pixler).
    private static final double SLOT_IMAGE_SIZE = 100;

    // Bredde på knappen under hvert slot.
    private static final double SLOT_BUTTON_WIDTH = 100;

    // Højde på knappen under hvert slot.
    private static final double SLOT_BUTTON_HEIGHT = 50;

    // Liste over de items, der lige nu vises (kan være filtreret).
    private ArrayList<Item> displayedItems = new ArrayList<>();

    @FXML
    private Parent root;   // Rod-node for denne scene (kun relevant ved manuel navigation).
    private Scene scene;   // Scene-reference (bruges hvis vi manuelt vil skifte scene).
    private Stage stage;   // Stage (vindue) som scenen vises i.

    @FXML
    private VBox inventoryVBox;   // Container, der holder alle rækker (HBox) med slots.

    @FXML
    private Label gold_Label;     // Viser spillerens nuværende guld.

    @FXML
    private Label weight_Label;   // Viser vægt / vægt-grænse.

    @FXML
    private Label pageLabel;      // Viser "side / antal sider".

    @FXML
    private Label slots_Label;    // Viser "brugte slots / tilgængelige slots".

    @FXML
    MenuButton weaponFilter_MenuButton = new MenuButton("Weapon Filter");   // Menu til våben-slot-filter.

    @FXML
    MenuButton itemTypeFilter_MenuButton = new MenuButton("Weapon Filter"); // Menu til generel item-type-filter.

    // MenuItems til våbentype-filter.
    private MenuItem filter_one_handed = new MenuItem("One-Handed");
    private MenuItem filter_two_handed = new MenuItem("Two-Handed");
    private MenuItem filter_dual_handed = new MenuItem("Dual-Handed");

    // MenuItems til itemtype-filter.
    private MenuItem filter_ItemType_Weapon = new MenuItem("Weapon");
    private MenuItem filter_ItemType_Armor = new MenuItem("Armor");
    private MenuItem filter_ItemType_Utility = new MenuItem("Utility");
    private MenuItem filter_ItemType_Consumable = new MenuItem("Consumable");

    @FXML
    public void initialize() {
        // Kører automatisk når FXML'en er loadet.
        // Her sætter vi filtre og deres klik-handlere op.
        menuButtonFilter();
    }

    // Kaldes udefra (via SceneNavigator) for at give controlleren adgang til Inventory.
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        // Opdater HUD, så guld/vægt passer til inventory.
        updateHud();
        // Start med at vise alle items fra inventaret.
        buildInventory(0, startDisplayInventory());
    }

    // Kaldes udefra (via SceneNavigator) for at give controlleren adgang til Shop.
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    // Bygger inventory-griddet dynamisk ud fra en liste af items og et startindex.
    private void buildInventory(int index, ArrayList<Item> displayedItems) {
        // Fjern alle tidligere rækker.
        inventoryVBox.getChildren().clear();

        // For hver række i inventoryet...
        for (int i = 0; i < ROWS; i++) {
            HBox row = new HBox(7);         // 7 = vandret afstand mellem slots.
            row.setAlignment(Pos.CENTER_LEFT);

            // For hver kolonne i denne række...
            for (int j = 0; j < COLS; j++) {
                // Hent item hvis index er i range, ellers er det et tomt slot.
                Item item = index < displayedItems.size() ? displayedItems.get(index) : null;
                // Lav et visuelt "kort" til slottet (billede + knap).
                VBox slot = createSlotCard(item);
                row.getChildren().add(slot);
                index++;                    // Gå videre til næste item.
            }

            // Tilføj rækken til den overordnede VBox.
            inventoryVBox.getChildren().add(row);
        }
    }

    // Opretter et visuelt "slotkort" til inventoryet: billede, antal-tekst og SELL-knap.
    private VBox createSlotCard(Item item) {
        // Yderste container for et enkelt slot.
        VBox card = new VBox(3);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(SLOT_IMAGE_SIZE);
        card.setMinWidth(SLOT_IMAGE_SIZE);
        card.setMaxWidth(SLOT_IMAGE_SIZE);
        card.getStyleClass().add("name_card");  // CSS-klasse til styling.

        // StackPane bruges til at lægge tekst oven på billedet.
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

        // Label der viser antal (stack size) for stackable items.
        Label amountLabel = new Label();
        amountLabel.setTextFill(Color.WHITE); // Hvid tekst så den kan ses på billedet.

        if (item != null) {
            // Find ItemName-enummen ud fra item’ets “display name”.
            ItemName itemName = ItemNameToItemNameRegistry.fromDisplayName(item.getName());
            // Slå stien til billedet op ud fra ItemName.
            String path = ItemImageRegistry.getDefinition(itemName);
            if (path != null) {
                URL url = getClass().getResource(path);
                if (url != null) {
                    // Indlæs og sæt billedet for dette slot.
                    imageView.setImage(new Image(url.toExternalForm()));
                }
            }
            // Vælg hvad der skal stå som antal afhængigt af typen.
            if (item instanceof Utility utility){
                amountLabel.setText(String.valueOf(utility.getStackSize()));
            }
            else if (item instanceof Consumable consumable){
                amountLabel.setText(String.valueOf(consumable.getStackSize()));
            } else if (item instanceof Weapon || item instanceof Armor){
                // Våben og rustninger stacker ikke, så vi viser 1.
                amountLabel.setText("1");
            }

        } else {
            // Hvis der ikke er noget item, kan vi vise et “tomt slot”-billede.
            URL url = getClass().getResource("/app/fxinventory/Images/empty_slot.png");
            if (url != null) {
                imageView.setImage(new Image(url.toExternalForm()));
            }
            amountLabel.setText("");
        }

        // Placér tekst nederst til venstre oven på billedet.
        StackPane.setAlignment(amountLabel, Pos.BOTTOM_LEFT);
        StackPane.setMargin(amountLabel, new Insets(0, 0, 3, 3));

        imageStack.getChildren().addAll(imageView, amountLabel);

        // Knap under billedet – bruges til at sælge item’et.
        Button slotButton = new Button(item != null ? "SELL" : "");
        slotButton.setPrefWidth(SLOT_BUTTON_WIDTH);
        slotButton.setMinWidth(SLOT_BUTTON_WIDTH);
        slotButton.setMaxWidth(SLOT_BUTTON_WIDTH);
        slotButton.setPrefHeight(SLOT_BUTTON_HEIGHT);

        if (item == null) {
            // Tomt slot → ingen handling, så knappen er disabled.
            slotButton.setDisable(true);
        } else {
            // Når der klikkes på knappen, sælges det konkrete item.
            slotButton.setOnAction(e -> onSlotSellClicked(item));
        }

        // Forhindrer at imageStack og knappen strækker sig lodret.
        VBox.setVgrow(imageStack, Priority.NEVER);
        VBox.setVgrow(slotButton, Priority.NEVER);

        // Tilføj billede + knap til kortet.
        card.getChildren().addAll(imageStack, slotButton);
        return card;
    }

    @FXML
    public void onSlotSellClicked(Item item) {
        // Sælger item’et via Shop-logikken (opdaterer guld, vægt, slots, stack osv.).
        shop.sellItem(inventory, item);
        // Opdater HUD efter salget.
        updateHud();
        // Genopbyg visningen på baggrund af det opdaterede inventory.
        displayedItems.clear();
        startDisplayInventory();
        buildInventory(0, displayedItems);
    }

    // Opdaterer HUD-elementer: guld, vægt, slots og side-indikator.
    private void updateHud() {
        if (gold_Label != null) {
            gold_Label.setText(String.valueOf(inventory.getGold()));
        }
        if (weight_Label != null) {
            double w = inventory.getWeight();
            // Undgå at vise -0.0 pga. floating point afrunding.
            if (Math.abs(w) < 1e-3) {
                w = 0.0;
            }
            weight_Label.setText(
                    String.format("%.1f/%d", w, inventory.getWeightLimit())
            );
        }
        if (slots_Label != null) {
            // Viser “antal brugte slots / antal tilgængelige slots”.
            slots_Label.setText(
                    String.valueOf(inventory.getCurrentSlotUsed())
                            + "/"
                            + String.valueOf(inventory.getAvailableSlots())
            );
        }

        // Viser aktuel side samt maks antal sider (her hårdkodet til 8).
        pageLabel.setText(inventoryPage + "/" + 8);
    }

    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        // Gå tilbage til Game_Home scenen.
        // Vi sender inventory og shop med, så state bevares.
        SceneNavigator.switchTo(event, "Game_Home.fxml", (Game_Home_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    @FXML
    public void onClickNextPage() {
        // Skift til næste side og beregn nyt startindex ud fra side og slots pr. side.
        inventoryPage++;
        index = (inventoryPage - 1) * 24;    // 24 = ROWS * COLS.
        buildInventory(index, displayedItems);
        updateHud();
    }

    @FXML
    public void onClickPreviousPage() {
        // Skift kun tilbage, hvis vi ikke allerede er på første side.
        if (inventoryPage > 1) {
            inventoryPage--;
        }
        index = (inventoryPage - 1) * 24;
        buildInventory(index, displayedItems);
        updateHud();
    }

    // Filtrerer inventoryet efter ItemType (Weapon, Armor, Utility, Consumable).
    // Resultatet gemmes i displayedItems og vises i griddet.
    public void filterByItemType(ItemType itemType) {
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item.getType() == itemType){
                displayedItems.add(item);
            }
        }
        buildInventory(0, displayedItems);
    }

    // Filtrerer inventoryet efter våben-slot-type (One-/Two-/Dual-handed).
    public void filterByWeaponSlot (WeaponSlotType slotType){
        displayedItems.clear();
        for (Item item : inventory.getItems()) {
            if (item instanceof Weapon weapon)
                if (weapon.getSlotType() == slotType) {
                    displayedItems.add(item);
                }
        }
        buildInventory(0, displayedItems);
    }

    // Starter visningen af inventoryet ved at kopiere alle items over i displayedItems.
    private ArrayList<Item> startDisplayInventory () {
        displayedItems.clear();
        displayedItems.addAll(inventory.getItems());
        return displayedItems;
    }

    // Initialiserer menuknapperne til filtrering og tilknytter deres event handlers.
    private void menuButtonFilter () {

        // Weapon filter menu: nulstil og sæt standardtekst.
        weaponFilter_MenuButton.getItems().clear();
        weaponFilter_MenuButton.setText("Weapon Filter");

        // Tilknyt handling til hver våbentype.
        filter_one_handed.setOnAction(e -> filterByWeaponSlot(WeaponSlotType.ONE_HANDED));
        filter_two_handed.setOnAction(e -> filterByWeaponSlot(WeaponSlotType.TWO_HANDED));
        filter_dual_handed.setOnAction(e -> filterByWeaponSlot(WeaponSlotType.DUAL_HANDED));

        weaponFilter_MenuButton.getItems().addAll(filter_one_handed, filter_two_handed, filter_dual_handed);

        // Item type filter menu: nulstil og sæt standardtekst.
        itemTypeFilter_MenuButton.getItems().clear();
        itemTypeFilter_MenuButton.setText("Item Type");

        // Tilknyt handling til hver itemtype.
        filter_ItemType_Weapon.setOnAction(e -> filterByItemType(ItemType.WEAPON));
        filter_ItemType_Armor.setOnAction(e -> filterByItemType(ItemType.ARMOR));
        filter_ItemType_Utility.setOnAction(e -> filterByItemType(ItemType.UTILITY));
        filter_ItemType_Consumable.setOnAction(e -> filterByItemType(ItemType.CONSUMABLE));

        itemTypeFilter_MenuButton.getItems().addAll(
                filter_ItemType_Weapon,
                filter_ItemType_Armor,
                filter_ItemType_Utility,
                filter_ItemType_Consumable
        );
    }
}
