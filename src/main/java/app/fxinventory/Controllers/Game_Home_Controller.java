package app.fxinventory.Controllers;

import app.fxinventory.Enums.ArmorSlotType;
import app.fxinventory.Enums.ItemName;
import app.fxinventory.Enums.ItemType;
import app.fxinventory.Enums.WeaponSlotType;
import app.fxinventory.Inventory.Inventory;
import app.fxinventory.Item.*;
import app.fxinventory.Main;
import app.fxinventory.Shop.Shop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class Game_Home_Controller {

    private Parent root;
    private Scene scene;
    private Stage stage;

    private final String url = "jdbc:mysql://localhost:3306/FXInventory_DB";
    private final String user = "root";
    private final String password = "";

    @FXML
    private Label gold_Label;
    @FXML
    private Label weight_Label;
    @FXML
    private Label slots_Label;
    @FXML
    private TextArea inventory_TextArea;

    private Inventory inventory;
    private Shop shop;

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        updateHud();
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }


    @FXML
    public void giveGold(){
        inventory.addGold(50);
        gold_Label.setText(String.valueOf(inventory.getGold()));
    }

    @FXML
    public void showShopScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Shop.fxml", (Shop_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    @FXML
    public void showInventoryScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Inventory.fxml", (Inventory_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    @FXML
    public void showUpgradeScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Upgrades.fxml", (Upgrades_Controller c) -> {
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
            slots_Label.setText(String.valueOf(inventory.getCurrentSlotUsed()) + "/" + String.valueOf(inventory.getAvailableSlots()));
        }
    }

    @FXML
    private void saveGame() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to save the game?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            String sqlTruncateInventory = "TRUNCATE Inventory_DB";

            String sqlTruncateInventoryMisc = "TRUNCATE InventoryMisc_DB";

            String sqlInventory = "INSERT INTO Inventory_DB " +
                    "(itemName, itemCost, itemWeight, itemType, " +
                    " armorSlotType, defencePoint, weaponSlotType, damage, stackSize) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String sqlInventoryMisc = "INSERT INTO InventoryMisc_DB " +
                    "(gold, weight, currentSlots, availableSlots, slotUpgradeOne, slotUpgradeTwo, slotUpgradeThree) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                try (PreparedStatement sqlTruncateInv = conn.prepareStatement(sqlTruncateInventory)) {
                    sqlTruncateInv.executeUpdate();
                }
                try (PreparedStatement sqlTruncateMisc = conn.prepareStatement(sqlTruncateInventoryMisc)) {
                    sqlTruncateMisc.executeUpdate();
                }

                try (PreparedStatement invStmt = conn.prepareStatement(sqlInventory)) {

                    for (Item item : inventory.getItems()) {

                        String itemName = item.getName();
                        int itemCost = item.getCost();
                        double itemWeight = item.getWeight();
                        ItemType itemType = item.getType();

                        ArmorSlotType armorSlotType = null;
                        Integer defencePoint = null;
                        WeaponSlotType weaponSlotType = null;
                        Integer damage = null;
                        int stackSize = 1;

                        if (item instanceof Armor armor) {
                            armorSlotType = armor.getSlotType();
                            defencePoint = armor.getDefencePoint();
                        } else if (item instanceof Weapon weapon) {
                            weaponSlotType = weapon.getSlotType();
                            damage = weapon.getDamage();
                        } else if (item instanceof Utility utility) {
                            stackSize = utility.getStackSize();
                        } else if (item instanceof Consumable consumable) {
                            stackSize = consumable.getStackSize();
                        }

                        invStmt.setString(1, itemName);
                        invStmt.setInt(2, itemCost);
                        invStmt.setDouble(3, itemWeight);
                        invStmt.setString(4, itemType.name());

                        if (armorSlotType != null) {
                            invStmt.setString(5, armorSlotType.name());
                            invStmt.setInt(6, defencePoint);
                        } else {
                            invStmt.setNull(5, java.sql.Types.VARCHAR);
                            invStmt.setNull(6, java.sql.Types.INTEGER);
                        }

                        if (weaponSlotType != null) {
                            invStmt.setString(7, weaponSlotType.name());
                            invStmt.setInt(8, damage);
                        } else {
                            invStmt.setNull(7, java.sql.Types.VARCHAR);
                            invStmt.setNull(8, java.sql.Types.INTEGER);
                        }

                        invStmt.setInt(9, stackSize);

                        invStmt.addBatch();
                    }

                    invStmt.executeBatch();
                }
                try (PreparedStatement miscStmt = conn.prepareStatement(sqlInventoryMisc)) {

                    miscStmt.setInt(1, inventory.getGold());
                    miscStmt.setDouble(2, inventory.getWeight());
                    miscStmt.setInt(3, inventory.getCurrentSlotUsed());
                    miscStmt.setInt(4, inventory.getAvailableSlots());
                    miscStmt.setInt(5, inventory.getSlotUpgradeOne());
                    miscStmt.setInt(6, inventory.getSlotUpgradeTwo());
                    miscStmt.setInt(7, inventory.getSlotUpgradeThree());

                    miscStmt.executeUpdate();
                }
            }
        }
    }

    @FXML
    private void loadGame() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to load the game? (Current progress will be lost)");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                String sqlMisc = "SELECT gold, weight, currentSlots, availableSlots, " +
                        "slotUpgradeOne, slotUpgradeTwo, slotUpgradeThree " +
                        "FROM InventoryMisc_DB ";

                try (PreparedStatement miscStmt = conn.prepareStatement(sqlMisc);
                     ResultSet miscRs = miscStmt.executeQuery()) {

                    if (miscRs.next()) {
                        inventory.setGold(miscRs.getInt("gold"));
                        inventory.setWeight(miscRs.getDouble("weight"));
                        inventory.setCurrentSlotUsed(miscRs.getInt("currentSlots"));
                        inventory.setAvailableSlots(miscRs.getInt("availableSlots"));
                        inventory.setSlotUpgradeOne(miscRs.getInt("slotUpgradeOne"));
                        inventory.setSlotUpgradeTwo(miscRs.getInt("slotUpgradeTwo"));
                        inventory.setSlotUpgradeThree(miscRs.getInt("slotUpgradeThree"));
                    }
                }

                inventory.clearItems();

                String sqlInv = "SELECT itemName, stackSize " +
                        "FROM Inventory_DB";

                try (PreparedStatement invStmt = conn.prepareStatement(sqlInv);
                     ResultSet invRs = invStmt.executeQuery()) {

                    while (invRs.next()) {
                        String name = invRs.getString("itemName");
                        int stackSize = invRs.getInt("stackSize");

                        ItemName itemNameEnum = ItemNameToItemNameRegistry.fromDisplayName(name);

                        Item item = ItemRegistry.getDefinition(itemNameEnum).createInstance();

                        if (item instanceof Utility utility) {

                            int diff = stackSize - utility.getStackSize();
                            if (diff != 0) {
                                utility.updateStackSize(diff);
                            }
                        } else if (item instanceof Consumable consumable) {
                            int diff = stackSize - consumable.getStackSize();
                            if (diff != 0) {
                                consumable.updateStackSize(diff);
                            }
                        }

                        inventory.addItemToInventory(item);
                    }
                }
            }

            updateHud();
        }
    }

}
