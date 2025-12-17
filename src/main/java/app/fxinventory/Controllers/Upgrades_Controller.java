package app.fxinventory.Controllers;

import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

// Controller for upgrade-skærmen.
// Ansvar: vise slot-upgrades, håndtere køb af opgraderinger og opdatere HUD.
public class Upgrades_Controller {

    // Reference til fælles Inventory (spillerens data, slots, upgrades osv.)
    private Inventory inventory;

    // Reference til fælles Shop (pt. ikke brugt her, men sendt videre ved "Back").
    private Shop shop;

    // HUD-labels til guld og vægt.
    @FXML
    private Label gold_Label;

    @FXML
    private Label weight_Label;

    // Labels til opgradering 1 (pris, navn og effekt-tekst).
    @FXML
    private Label SlotUpgradeOne_Price_Label;
    @FXML
    private Label SlotUpgradeOne_Name_Label;
    @FXML
    private Label SlotUpgradeOne_Effect_Label;

    // Labels til opgradering 2.
    @FXML
    private Label SlotUpgradeTwo_Price_Label;
    @FXML
    private Label SlotUpgradeTwo_Name_Label;
    @FXML
    private Label SlotUpgradeTwo_Effect_Label;

    // Labels til opgradering 3.
    @FXML
    private Label SlotUpgradeThree_Price_Label;
    @FXML
    private Label SlotUpgradeThree_Name_Label;
    @FXML
    private Label SlotUpgradeThree_Effect_Label;

    // Label der viser brugte slots / tilgængelige slots.
    @FXML
    private Label slots_Label;

    // Kaldes udefra for at injecte Inventory ind i controlleren.
    // Når inventory er sat, opdateres HUD med aktuelle værdier.
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        updateHud();
    }

    // Kaldes udefra for at injecte Shop (videreført til Game_Home ved "Back").
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    // Tilbage-knap: skifter tilbage til Game_Home.fxml
    // og tager Inventory og Shop med videre via SceneNavigator.
    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Game_Home.fxml", (Game_Home_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    // Opdaterer HUD-labels (guld, vægt, slots) ud fra inventory-objektet.
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
            // Viser "brugte slots / maks slots".
            slots_Label.setText(String.valueOf(inventory.getCurrentSlotUsed())+ "/" + String.valueOf(inventory.getAvailableSlots()));
        }
    }

    // Fælles metode til at håndtere køb af en slot-upgrade.
    // Bruges af alle tre opgraderingers onClick-metoder, så logikken ikke duplikeres.
    private void handleSlotUpgrade(
            int currentLevel,               // Nuværende "level" for denne upgrade (fx 1 = ikke købt, 2 = købt)
            int maxLevel,                   // Max "level" (typisk 2 → købt/aktiveret)
            int cost,                       // Pris i guld
            int slotsEffect,                // Hvor mange ekstra slots denne upgrade giver
            String upgradeNameText,         // Tekst til navn-label (fx "Slot Upgrade: I")
            Label nameLabel,                // Label der viser navnet på upgraden
            Label priceLabel,               // Label der viser pris-tekst
            Label effectLabel,              // Label der viser effekt-tekst
            java.util.function.IntConsumer levelSetter // Lambda, der gemmer det nye level i Inventory
    ) {
        // Tjek om spilleren allerede har købt denne opgradering / nået max-niveau.
        if (currentLevel >= maxLevel) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Du har allerede købt denne opgradering");
            alert.showAndWait();
            return;
        }

        // Tjek om spilleren har nok guld til at købe opgraderingen.
        if (inventory.getGold() < cost) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Du har ikke nok guld");
            alert.setContentText("Du mangler " + (cost - inventory.getGold()) + " guld.");
            alert.showAndWait();
            return;
        }

        // Selve købet: giv flere slots og træk guld.
        inventory.updateAvailableSlots(slotsEffect); // Forøger antal tilgængelige slots.
        inventory.addGold(-cost);                    // Trækker guld for opgraderingen.

        // Sæt upgrade-level til maxLevel (markerer at den nu er købt/fuldt opgraderet).
        levelSetter.accept(maxLevel);

        // Opdater labels for denne upgrade, så UI afspejler købet.
        nameLabel.setText(upgradeNameText);
        priceLabel.setText("Cost: " + cost + " Guld");
        effectLabel.setText("Effect: +" + slotsEffect + " slots");

        // Opdater HUD (guld, slots osv.) efter købet.
        updateHud();
    }

    // Klik-handler for første slot-upgrade.
    @FXML
    private void onUpgradeSlotOne() {
        handleSlotUpgrade(
                inventory.getSlotUpgradeOne(),        // currentLevel fra Inventory
                2,                                     // maxLevel (1 = ikke købt, 2 = købt)
                300,                                   // cost i guld
                30,                                    // slotsEffect (giver +30 slots)
                "Slot Upgrade: I",                     // navn-tekst til UI
                SlotUpgradeOne_Name_Label,             // nameLabel
                SlotUpgradeOne_Price_Label,            // priceLabel
                SlotUpgradeOne_Effect_Label,           // effectLabel
                level -> inventory.setSlotUpgradeOne(level) // Gem nyt level i Inventory
        );
    }

    // Klik-handler for anden slot-upgrade (dyrere og større effekt).
    @FXML
    private void onUpgradeSlotTwo() {
        handleSlotUpgrade(
                inventory.getSlotUpgradeTwo(),         // currentLevel
                2,                                     // maxLevel
                1000,                                  // cost
                50,                                    // slotsEffect (giver +50 slots)
                "Slot Upgrade: II",                    // navn-tekst
                SlotUpgradeTwo_Name_Label,             // nameLabel
                SlotUpgradeTwo_Price_Label,            // priceLabel
                SlotUpgradeTwo_Effect_Label,           // effectLabel
                level -> inventory.setSlotUpgradeTwo(level) // Gem i Inventory
        );
    }

    // Klik-handler for tredje slot-upgrade (dyrest og størst effekt).
    @FXML
    private void onUpgradeSlotThree() {
        handleSlotUpgrade(
                inventory.getSlotUpgradeThree(),       // currentLevel
                2,                                     // maxLevel
                2500,                                  // cost
                80,                                    // slotsEffect (giver +80 slots)
                "Slot Upgrade: III",                   // navn-tekst
                SlotUpgradeThree_Name_Label,           // nameLabel
                SlotUpgradeThree_Price_Label,          // priceLabel
                SlotUpgradeThree_Effect_Label,         // effectLabel
                level -> inventory.setSlotUpgradeThree(level) // Gem i Inventory
        );
    }

}
