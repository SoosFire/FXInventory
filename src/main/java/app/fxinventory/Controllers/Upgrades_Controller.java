package app.fxinventory.Controllers;

import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private Button slotUpgrade_Button;
    @FXML
    private Label slotUpgradeLevel_Label;
    @FXML
    private Label slotUpgradeCost_Label;
    @FXML
    private Label slotUpgradeEffect_Label;

    int slotUpgradeCost = 300;
    int slotUpgradeLevel = 1;
    int slotUpgradeEffect = 30;
    int slotUpgradeMax = 3;

    @FXML
    public void initialize() {
        updateSlotUpgrade();
    }


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
    }

    @FXML
    private void onUpgradeSlotOne () {
        if (slotUpgradeLevel <= slotUpgradeMax) {
            if (inventory.getGold() >= slotUpgradeCost) {
                inventory.updateAvailableSlots(slotUpgradeEffect);
                inventory.addGold(-slotUpgradeCost);

                slotUpgradeLevel++;
                slotUpgradeCost += 300;
                slotUpgradeEffect += 27;

                if (slotUpgradeLevel > slotUpgradeMax) {
                    slotUpgrade_Button.setOpacity(0.2);
                    slotUpgrade_Button.setDisable(true);
                } else {
                    updateSlotUpgrade();
                }
                updateHud();

            }
        }
    }

    private void updateSlotUpgrade(){
        slotUpgradeLevel_Label.setText("Level: " + slotUpgradeLevel);
        slotUpgradeCost_Label.setText("Cost: " + slotUpgradeCost);
        slotUpgradeEffect_Label.setText("Effect: +" + slotUpgradeEffect + " slots");
    }
}
