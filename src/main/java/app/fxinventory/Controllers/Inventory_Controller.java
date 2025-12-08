package app.fxinventory.Controllers;

import app.fxinventory.Item.ItemNameToItemNameRegistry;

import app.fxinventory.Enums.ItemName;
import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Item.Item;
import app.fxinventory.Item.ItemImageRegistry;
import app.fxinventory.Main;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Objects;

public class Inventory_Controller {

    private final Inventory inventory = Main.getInventory();

    // 3 rows x 6 columns = 18 slots total
    private static final int ROWS = 3;
    private static final int COLS = 8;

    private static final double SLOT_IMAGE_SIZE = 100;
    private static final double SLOT_BUTTON_WIDTH = 100;
    private static final double SLOT_BUTTON_HEIGHT = 50;

    @FXML
    private VBox inventoryVBox;

    @FXML
    private Label gold_Label;

    @FXML
    private Label weight_Label;

    @FXML
    public void initialize() {
        updateHud();
        buildInventory();
    }

    private void buildInventory() {
        inventoryVBox.getChildren().clear();

        List<Item> items = inventory.getItems();
        int index = 0;

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

            amountLabel.setText(String.valueOf(item.getTotalAmount()));
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
            slotButton.setOnAction(e -> onSlotButtonClicked(item));
        }

        VBox.setVgrow(imageStack, Priority.NEVER);
        VBox.setVgrow(slotButton, Priority.NEVER);

        card.getChildren().addAll(imageStack, slotButton);
        return card;
    }

    // TO DO
    @FXML
    public void onSlotButtonClicked(Item item) {
        System.out.println("Clicked inventory item: " + item.getName());
    }

    private void updateHud() {
        if (gold_Label != null) {
            gold_Label.setText(String.valueOf(inventory.getGold()));
        }
        if (weight_Label != null) {
            weight_Label.setText(inventory.getWeight() + "/50");
        }
    }
}
