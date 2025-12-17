package app.fxinventory.Controllers;

import app.fxinventory.Enums.ItemName;
import app.fxinventory.Enums.ItemType;
import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Item.*;
import app.fxinventory.Main;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

// Controller for shop-skærmen.
// Ansvar: vise varer til salg, håndtere køb, opdatere HUD og kunne refresh’e shoppen.
public class Shop_Controller {

    // Reference til fælles Inventory (spillerens data).
    private Inventory inventory;

    // Reference til Shop-logikken, som håndterer selve køb-logikken.
    private Shop shop;

    // Referencer til scene og stage (brugt ved manuel sceneskift, hvis nødvendigt).
    private Parent root;
    private Scene scene;
    private Stage stage;

    // Antal rækker og kolonner i shop-griddet (3x3 = 9 varer pr. refresh).
    private static final int ROWS = 3;
    private static final int COLS = 3;

    // Visuel størrelse på hvert shop-kort (baggrund med tekst og knap).
    private static final double CARD_WIDTH = 290;
    private static final double CARD_HEIGHT = 125;
    private static final double BUY_BUTTON_WIDTH = 82;

    // Størrelse på item-billedet i hvert kort.
    private static final double ITEM_IMAGE_SIZE = 70;

    // Random-generator til at vælge tilfældige items til shoppen.
    private final Random random = new Random();

    // HUD-labels der vises på shop-skærmen.
    @FXML
    private Label gold_Label;
    @FXML
    private Label weight_Label;
    @FXML
    private Label slots_Label;

    // Kan bruges til debug / visning af inventory-info om nødvendigt.
    @FXML
    private TextArea inventory_TextArea;

    // VBox der indeholder rækkerne (HBox) af shop-kort.
    @FXML
    private VBox shopVBox;

    // Kaldes udefra for at injecte Inventory ind i controlleren.
    // Opdaterer også HUD og bygger shoppen første gang.
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        updateHud();
        buildShop();
    }

    // Kaldes udefra for at injecte Shop-indstansen ind i controlleren.
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    // Tilbage-knap: skifter scene tilbage til Game_Home.fxml
    // og sender Inventory og Shop med videre.
    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Game_Home.fxml", (Game_Home_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    // Bygger shop-oversigten med 3x3 kort, hver med et tilfældigt item.
    public void buildShop () {
        // Ryd tidligere indhold.
        shopVBox.getChildren().clear();

        // Alle mulige items, som shoppen kan vælge imellem.
        ItemName[] allItems = ItemName.values();

        // Opbyg ROWS rækker.
        for (int i = 0; i < ROWS; i++) {
            HBox row = new HBox(10);  // Vandret afstand mellem kortene.
            // I hver række tilføjes COLS kort.
            for (int j = 0; j < COLS; j++) {
                // Vælg et tilfældigt ItemName til denne plads.
                ItemName itemName = allItems[random.nextInt(allItems.length)];
                // Opret et visuelt kort for dette item.
                Node card = createItemCard(itemName);
                row.getChildren().add(card);
            }
            // Tilføj rækken til den overordnede VBox.
            shopVBox.getChildren().add(row);
        }
    }

    // Bygger et enkelt shop-kort for et givet ItemName.
    // Kortet indeholder billede, navn, stats og en (usynlig) BUY-knap over et taske-billede.
    private Node createItemCard(ItemName itemName) {
        // Opret en instans af item’et for at kunne læse stats (cost, weight, damage osv.).
        Item item = ItemRegistry.getDefinition(itemName).createInstance();

        // Yderste kort (vandret layout: billede | info | spacer | køb-ikon).
        HBox card = new HBox(7);
        card.setPrefWidth(CARD_WIDTH);
        card.setMinWidth(CARD_WIDTH);
        card.setMaxWidth(CARD_WIDTH);
        card.setPrefHeight(CARD_HEIGHT);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(5, 5, 5, 5));

        // Indlæs baggrundsbillede til kortet.
        Image bgImage = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/app/fxinventory/Images/barmid_ready.png")
                ).toExternalForm()
        );

        BackgroundSize bgSize = new BackgroundSize(
                CARD_WIDTH, CARD_HEIGHT,
                false, false,
                false, false
        );

        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize
        );

        // Sæt baggrundsbilledet på kortet.
        card.setBackground(new Background(backgroundImage));

        // Venstre side: info om item (navn, pris, evt. damage/defence).
        VBox infoBox = new VBox(2);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setFillWidth(true);

        Label nameLabel = new Label(item.getName());
        nameLabel.getStyleClass().add("item_description");

        Label costLabel = new Label("Cost: " + item.getCost());
        costLabel.getStyleClass().add("item_description");

        Label weightLabel = new Label("Weight: " + item.getWeight());
        weightLabel.getStyleClass().add("item_description");

        // Tilføj basis-info til infoBox (navn + cost).
        infoBox.getChildren().addAll(nameLabel, costLabel);

        // Hvis det er et våben, vis skade.
        if (item instanceof Weapon weapon){
            Label dmgLabel = new Label("Damage: " + weapon.getDamage());
            dmgLabel.getStyleClass().add("item_description");
            infoBox.getChildren().add(dmgLabel);
        }

        // Hvis det er en rustning, vis defence.
        if (item instanceof Armor armor){
            Label armorLabel = new Label("Defence: " + armor.getDefencePoint());
            armorLabel.getStyleClass().add("item_description");
            infoBox.getChildren().add(armorLabel);
        }

        // Spacer til at skubbe køb-ikonet helt til højre.
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Selve BUY-knappen (gør den gennemsigtig, da vi viser et billede ovenpå).
        Button buyButton = new Button("Buy");
        buyButton.setPrefWidth(BUY_BUTTON_WIDTH - 20);
        buyButton.setMinWidth(BUY_BUTTON_WIDTH);
        buyButton.setMaxWidth(BUY_BUTTON_WIDTH);
        buyButton.setPrefHeight(CARD_HEIGHT - 20);
        buyButton.setAlignment(Pos.CENTER);
        buyButton.setOpacity(0);  // Usynlig, men stadig klikbar.

        // Når der trykkes på BUY:
        buyButton.setOnAction(e -> {
            int cost = item.getCost();
            // Tjek om spilleren har nok guld.
            if (inventory.getGold() >= cost) {
                // Tjek om item’et overskrider vægtgrænsen.
                if ((inventory.getWeight() + item.getWeight()) <= inventory.getWeightLimit()) {
                    // Tjek om der er nok ledige slots til at tilføje item’et.
                    if (inventory.getCurrentSlotUsed() < inventory.getAvailableSlots()){
                        // Brug Shop-logikken til faktisk at købe item’et (stacking, slots osv.).
                        shop.buyItem(inventory, itemName);
                        updateHud();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Slot limit reached!");
                        alert.setContentText("You dont have enough space to buy this item!");
                        alert.showAndWait();
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Weight limit!");
                    alert.setContentText("This item exceeds the weight limit!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Insufficient gold!");
                alert.setContentText("You don't have enough gold to buy this item");
                alert.showAndWait();
            }
        });

        // Group bruges til at lægge en usynlig knap ovenpå et billede (taske-ikon).
        Group buyButtonGroup = new Group();

        // Taske-billede fungerer som “grafisk køb-knap”.
        ImageView imageSatchel = new ImageView(getClass().getResource("/app/fxinventory/Images/satchel.png").toExternalForm());
        imageSatchel.setFitHeight(CARD_HEIGHT - 20);
        imageSatchel.setFitWidth(BUY_BUTTON_WIDTH - 20);
        imageSatchel.setPreserveRatio(true);
        imageSatchel.setDisable(true); // Billedet skal ikke reagere på klik, det gør knappen.

        // Læg knap og billede oven i hinanden (knappen er klikbar, billedet er synligt).
        buyButtonGroup.getChildren().add(buyButton);
        buyButtonGroup.getChildren().add(imageSatchel);

        // Billede af selve item’et til venstre på kortet.
        ImageView itemImage = new ImageView(
                Objects.requireNonNull(
                        getClass().getResource(ItemImageRegistry.getDefinition(itemName))
                ).toExternalForm()
        );

        itemImage.setFitWidth(ITEM_IMAGE_SIZE);
        itemImage.setFitHeight(ITEM_IMAGE_SIZE);
        itemImage.setPreserveRatio(true);
        itemImage.setSmooth(true);
        itemImage.setCache(true);

        // Til sidst samles kortet: [item-billede] [info-tekst] [spacer] [taske/BUY].
        card.getChildren().addAll(itemImage, infoBox, spacer, buyButtonGroup);
        return card;
    }

    // Handler til en "Refresh Shop"-knap.
    // Koster 100 guld at få nye tilfældige varer.
    @FXML
    private void onRefreshShop() {
        int refreshCost = 100;
        if (inventory.getGold() >= refreshCost) {
            inventory.addGold(-refreshCost);
            updateHud();
            buildShop();  // Byg shoppen igen med nye random items.
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Insufficient gold!");
            alert.setContentText("You don't have enough gold to refresh the shop");
            alert.showAndWait();
        }
    }

    // Opdaterer HUD med guld, vægt og slots ud fra inventory.
    private void updateHud() {
        if (gold_Label != null) {
            gold_Label.setText(String.valueOf(inventory.getGold()));
        }
        if (weight_Label != null) {
            double w = inventory.getWeight();
            // Undgå "-0.0" på grund af floating point afrunding.
            if (Math.abs(w) < 1e-3) {
                w = 0.0;
            }
            weight_Label.setText(
                    String.format("%.1f/%d", w, inventory.getWeightLimit())
            );
        }
        if (slots_Label != null) {
            // Viser "brugte slots / maksimale slots".
            slots_Label.setText(String.valueOf(inventory.getCurrentSlotUsed()) + "/" + String.valueOf(inventory.getAvailableSlots()));
        }
    }
}
