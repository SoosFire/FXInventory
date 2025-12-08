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

public class Shop_Controller {

    private Parent root;
    private Scene scene;
    private Stage stage;

    private final Inventory inventory = Main.getInventory();
    private final Shop shop = Main.getShop();

    private static final int ROWS = 3;
    private static final int COLS = 3;

    private static final double CARD_WIDTH = 290;
    private static final double CARD_HEIGHT = 125;
    private static final double BUY_BUTTON_WIDTH = 82;

    private static final double ITEM_IMAGE_SIZE = 70; // or whatever you like


    private final Random random = new Random();

    @FXML
    private Label gold_Label;
    @FXML
    private Label weight_Label;
    @FXML
    private TextArea inventory_TextArea;

    @FXML
    private VBox shopVBox;

    @FXML
    public void initialize() {
        updateHud();
        buildShop();
    }

    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("Game_Home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,1000,750);
        stage.setScene(scene);
        stage.show();
    }

    public void buildShop () {
        shopVBox.getChildren().clear();

        ItemName[] allItems = ItemName.values();

        for (int i = 0; i < ROWS; i++) {
            HBox row = new HBox(10);
            for (int j = 0; j < COLS; j++) {
                ItemName itemName = allItems[random.nextInt(allItems.length)];
                Node card = createItemCard(itemName);
                row.getChildren().add(card);
            }
            shopVBox.getChildren().add(row);
        }
    }

    private Node createItemCard(ItemName itemName) {
        // Create an item instance just to read its stats
        Item item = ItemRegistry.getDefinition(itemName).createInstance();

        // Outer card
        HBox card = new HBox(7);
        card.setPrefWidth(CARD_WIDTH);
        card.setMinWidth(CARD_WIDTH);
        card.setMaxWidth(CARD_WIDTH);
        card.setPrefHeight(CARD_HEIGHT);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(5, 5, 5, 5));

        // ---- NEW: set background image ----
        Image bgImage = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/app/fxinventory/Images/barmid_ready.png")
                ).toExternalForm()
        );

        BackgroundSize bgSize = new BackgroundSize(
                CARD_WIDTH, CARD_HEIGHT,
                false, false,   // percentage
                false, false   // cover = true
        );


        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize
        );

        card.setBackground(new Background(backgroundImage));

        // Left: labels (name, cost, maybe dmg/armor)
        VBox infoBox = new VBox(2);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setFillWidth(true);

        Label nameLabel = new Label(item.getName());
        nameLabel.getStyleClass().add("item_description");

        Label costLabel = new Label("Cost: " + item.getCost());
        costLabel.getStyleClass().add("item_description");

        Label weightLabel = new Label("Weight: " + item.getWeight());
        weightLabel.getStyleClass().add("item_description");

        infoBox.getChildren().addAll(nameLabel, costLabel);

        if (item instanceof Weapon weapon){
            Label dmgLabel = new Label("Damage: " + weapon.getDamage());
            dmgLabel.getStyleClass().add("item_description");
            infoBox.getChildren().add(dmgLabel);
        }

        if (item instanceof Armor armor){
            Label armorLabel = new Label("Defence: " + armor.getDefencePoint());
            armorLabel.getStyleClass().add("item_description");
            infoBox.getChildren().add(armorLabel);
        }
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button buyButton = new Button("Buy");
        buyButton.setPrefWidth(BUY_BUTTON_WIDTH - 20);
        buyButton.setMinWidth(BUY_BUTTON_WIDTH);
        buyButton.setMaxWidth(BUY_BUTTON_WIDTH);
        buyButton.setPrefHeight(CARD_HEIGHT - 20); // a bit smaller than card
        buyButton.setAlignment(Pos.CENTER);
        buyButton.setOpacity(0);

        buyButton.setOnAction(e -> {
            int cost = item.getCost();
            if (inventory.getGold() >= cost) {

                shop.buyItem(inventory, itemName);
                updateHud();

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Insufficient gold!");
                alert.setContentText("You don't have enough gold to buy this item");
                alert.showAndWait();
            }
        });

        Group buyButtonGroup = new Group();
        ImageView imageSatchel = new ImageView(getClass().getResource("/app/fxinventory/Images/satchel.png").toExternalForm());
        imageSatchel.setFitHeight(CARD_HEIGHT - 20);
        imageSatchel.setFitWidth(BUY_BUTTON_WIDTH - 20);
        imageSatchel.setPreserveRatio(true);
        imageSatchel.setDisable(true);

        buyButtonGroup.getChildren().add(buyButton);
        buyButtonGroup.getChildren().add(imageSatchel);

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

        card.getChildren().addAll(itemImage,infoBox, spacer, buyButtonGroup);
        return card;
    }

    @FXML
    private void onRefreshShop() {
        int refreshCost = 100;
        if (inventory.getGold() >= refreshCost) {
            inventory.addGold(-refreshCost);
            updateHud();
            buildShop();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Insufficient gold!");
            alert.setContentText("You don't have enough gold to refresh the shop");
            alert.showAndWait();
        }
    }

    private void updateHud() {
        gold_Label.setText(String.valueOf(inventory.getGold()));
        weight_Label.setText(inventory.getWeight() + "/50");
    }
}
