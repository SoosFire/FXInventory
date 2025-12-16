package app.fxinventory.Controllers;

import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class Upgrades_Controller {

    private Inventory inventory;
    private Shop shop;

    @FXML
    private Label gold_Label;

    @FXML
    private Label weight_Label;


    @FXML
    private Label SlotUpgradeOne_Price_Label;
    @FXML
    private Label SlotUpgradeOne_Name_Label;
    @FXML
    private Label SlotUpgradeOne_Effect_Label;

    @FXML
    private Label SlotUpgradeTwo_Price_Label;
    @FXML
    private Label SlotUpgradeTwo_Name_Label;
    @FXML
    private Label SlotUpgradeTwo_Effect_Label;

    @FXML
    private Label SlotUpgradeThree_Price_Label;
    @FXML
    private Label SlotUpgradeThree_Name_Label;
    @FXML
    private Label SlotUpgradeThree_Effect_Label;

    @FXML
    private Label slots_Label;

    int slotUpgradeCost = 300;
    int slotUpgradeLevel = 1;
    int slotUpgradeEffect = 30;
    int slotUpgradeMax = 3;


    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        updateHud();
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Game_Home.fxml", (Game_Home_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

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
        if (slots_Label != null) {
            slots_Label.setText(String.valueOf(inventory.getCurrentSlotUsed())+ "/" + String.valueOf(inventory.getAvailableSlots()));
        }
    }

    // Fælles metode til at håndtere køb af en slot-upgrade
    private void handleSlotUpgrade(
            int currentLevel,               // nuværende "level" for denne upgrade
            int maxLevel,                   // max "level" (eller købt-status)
            int cost,                       // pris i guld
            int slotsEffect,                // hvor mange ekstra slots man får
            String upgradeNameText,         // tekst til navn-label ("Slot Upgrade: I")
            Label nameLabel,                // den label, der viser navn
            Label priceLabel,               // den label, der viser pris
            Label effectLabel,              // den label, der viser effekt-tekst
            java.util.function.IntConsumer levelSetter // hvordan vi gemmer det nye level i inventory
    ) {
        // Allerede købt / max niveau nået?
        if (currentLevel >= maxLevel) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Du har allerede købt denne opgradering");
            alert.showAndWait();
            return;
        }

        // Har spilleren råd?
        if (inventory.getGold() < cost) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Du har ikke nok guld");
            alert.setContentText("Du mangler " + (cost - inventory.getGold()) + " guld.");
            alert.showAndWait();
            return;
        }

        // Udfør selve købet
        inventory.updateAvailableSlots(slotsEffect); // giver flere slots
        inventory.addGold(-cost);                    // trækker guld

        // Sæt level til maxLevel (typisk "købt"-state)
        levelSetter.accept(maxLevel);

        // Opdater labels for denne upgrade
        nameLabel.setText(upgradeNameText);
        priceLabel.setText("Cost: " + cost + " Guld");
        effectLabel.setText("Effect: +" + slotsEffect + " slots");

        // Opdater HUD (guld/vægt)
        updateHud();
    }

    @FXML
    private void onUpgradeSlotOne() {
        handleSlotUpgrade(
                inventory.getSlotUpgradeOne(),        // currentLevel
                2,                                     // maxLevel (1 = ikke købt, 2 = købt)
                300,                                   // cost
                30,                                    // slotsEffect
                "Slot Upgrade: I",                     // navn-tekst
                SlotUpgradeOne_Name_Label,             // nameLabel
                SlotUpgradeOne_Price_Label,            // priceLabel
                SlotUpgradeOne_Effect_Label,           // effectLabel
                level -> inventory.setSlotUpgradeOne(level) // sådan gemmer vi level
        );
    }

    @FXML
    private void onUpgradeSlotTwo() {
        handleSlotUpgrade(
                inventory.getSlotUpgradeTwo(),        // currentLevel
                2,                                     // maxLevel (1 = ikke købt, 2 = købt)
                1000,                                   // cost
                50,                                    // slotsEffect
                "Slot Upgrade: II",                     // navn-tekst
                SlotUpgradeTwo_Name_Label,             // nameLabel
                SlotUpgradeTwo_Price_Label,            // priceLabel
                SlotUpgradeTwo_Effect_Label,           // effectLabel
                level -> inventory.setSlotUpgradeTwo(level) // sådan gemmer vi level
        );
    }

    @FXML
    private void onUpgradeSlotThree() {
        handleSlotUpgrade(
                inventory.getSlotUpgradeThree(),        // currentLevel
                2,                                     // maxLevel (1 = ikke købt, 2 = købt)
                2500,                                   // cost
                80,                                    // slotsEffect
                "Slot Upgrade: III",                     // navn-tekst
                SlotUpgradeThree_Name_Label,             // nameLabel
                SlotUpgradeThree_Price_Label,            // priceLabel
                SlotUpgradeThree_Effect_Label,           // effectLabel
                level -> inventory.setSlotUpgradeThree(level) // sådan gemmer vi level
        );
    }

}
