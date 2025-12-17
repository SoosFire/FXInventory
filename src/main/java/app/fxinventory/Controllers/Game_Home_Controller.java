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

// Controller for "Game Home"-skærmen.
// Ansvar: navigere til andre scener (Shop, Inventory, Upgrades),
// vise HUD (guld, vægt, slots) og håndtere save/load af spillet til databasen.
public class Game_Home_Controller {

    // Referencer til nuværende scene og stage (bruges hvis man manuelt vil skifte).
    private Parent root;
    private Scene scene;
    private Stage stage;

    // Databaseforbindelsesoplysninger (MySQL URL, brugernavn og password).
    private final String url = "jdbc:mysql://localhost:3306/FXInventory_DB";
    private final String user = "root";
    private final String password = "";

    // HUD-labels som viser spillerens status.
    @FXML
    private Label gold_Label;
    @FXML
    private Label weight_Label;
    @FXML
    private Label slots_Label;

    // Tekstområde der kan bruges til debug eller visning af inventory-info.
    @FXML
    private TextArea inventory_TextArea;

    // Reference til det fælles Inventory-objekt (spillerens items og stats).
    private Inventory inventory;

    // Reference til det fælles Shop-objekt.
    private Shop shop;

    // Kaldes udefra (fx fra Main eller SceneNavigator) for at give controlleren Inventory.
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        // Opdater HUD så snart inventory er sat.
        updateHud();
    }

    // Kaldes udefra for at give controlleren Shop.
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    // Lille hjælpefunktion til test – giver spilleren 50 guld.
    @FXML
    public void giveGold(){
        inventory.addGold(50);
        gold_Label.setText(String.valueOf(inventory.getGold()));
    }

    // Skift til Shop-scenen, og tag Inventory og Shop med over til Shop_Controller.
    @FXML
    public void showShopScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Shop.fxml", (Shop_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    // Skift til Inventory-scenen, og injicér Inventory og Shop.
    @FXML
    public void showInventoryScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Inventory.fxml", (Inventory_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    // Skift til Upgrades-scenen, og injicér Inventory og Shop.
    @FXML
    public void showUpgradeScene(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Upgrades.fxml", (Upgrades_Controller c) -> {
            c.setInventory(inventory);
            c.setShop(shop);
        });
    }

    // Opdaterer HUD-elementer (guld, vægt og slots) baseret på inventory.
    private void updateHud() {
        if (gold_Label != null) {
            gold_Label.setText(String.valueOf(inventory.getGold()));
        }
        if (weight_Label != null) {
            double w = inventory.getWeight();
            // Undgå at vise -0.0, som kan opstå pga. floating point afrunding.
            if (Math.abs(w) < 1e-3) {
                w = 0.0;
            }
            weight_Label.setText(
                    String.format("%.1f/%d", w, inventory.getWeightLimit())
            );
        }
        if (slots_Label != null) {
            // Viser "aktuelle brugte slots / samlede slots".
            slots_Label.setText(String.valueOf(inventory.getCurrentSlotUsed()) + "/" + String.valueOf(inventory.getAvailableSlots()));
        }
    }

    // Gemmer spillet til databasen (Inventory_DB og InventoryMisc_DB).
    @FXML
    private void saveGame() throws SQLException {
        // Bekræftelses-popup før der gemmes.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to save the game?");

        Optional<ButtonType> result = alert.showAndWait();

        // Kun gem hvis brugeren trykker OK.
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // SQL til at tømme tabellerne, så vi ikke får dubletter.
            String sqlTruncateInventory = "TRUNCATE Inventory_DB";
            String sqlTruncateInventoryMisc = "TRUNCATE InventoryMisc_DB";

            // SQL til at indsætte items fra inventory.
            String sqlInventory = "INSERT INTO Inventory_DB " +
                    "(itemName, itemCost, itemWeight, itemType, " +
                    " armorSlotType, defencePoint, weaponSlotType, damage, stackSize) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // SQL til at indsætte generel inventory-info (guld, vægt, slots, upgrades).
            String sqlInventoryMisc = "INSERT INTO InventoryMisc_DB " +
                    "(gold, weight, currentSlots, availableSlots, slotUpgradeOne, slotUpgradeTwo, slotUpgradeThree) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Opretter databaseforbindelse.
            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                // Tømmer Inventory_DB tabellen.
                try (PreparedStatement sqlTruncateInv = conn.prepareStatement(sqlTruncateInventory)) {
                    sqlTruncateInv.executeUpdate();
                }
                // Tømmer InventoryMisc_DB tabellen.
                try (PreparedStatement sqlTruncateMisc = conn.prepareStatement(sqlTruncateInventoryMisc)) {
                    sqlTruncateMisc.executeUpdate();
                }

                // Gemmer alle items fra inventory i Inventory_DB.
                try (PreparedStatement invStmt = conn.prepareStatement(sqlInventory)) {

                    for (Item item : inventory.getItems()) {

                        String itemName = item.getName();
                        int itemCost = item.getCost();
                        double itemWeight = item.getWeight();
                        ItemType itemType = item.getType();

                        // Felter til rustning og våben – initialiseres til null.
                        ArmorSlotType armorSlotType = null;
                        Integer defencePoint = null;
                        WeaponSlotType weaponSlotType = null;
                        Integer damage = null;

                        // Standard stackSize er 1 (for ikke-stackable items).
                        int stackSize = 1;

                        // Hvis item er Armor, udfyld rustningsfelter.
                        if (item instanceof Armor armor) {
                            armorSlotType = armor.getSlotType();
                            defencePoint = armor.getDefencePoint();
                            // Hvis item er Weapon, udfyld våbenfelter.
                        } else if (item instanceof Weapon weapon) {
                            weaponSlotType = weapon.getSlotType();
                            damage = weapon.getDamage();
                            // Hvis Utility eller Consumable, brug deres stackSize.
                        } else if (item instanceof Utility utility) {
                            stackSize = utility.getStackSize();
                        } else if (item instanceof Consumable consumable) {
                            stackSize = consumable.getStackSize();
                        }

                        // Sæt fælles felter (navn, pris, vægt, type).
                        invStmt.setString(1, itemName);
                        invStmt.setInt(2, itemCost);
                        invStmt.setDouble(3, itemWeight);
                        invStmt.setString(4, itemType.name());

                        // Hvis vi har rustnings-info, sæt den – ellers indsæt NULL.
                        if (armorSlotType != null) {
                            invStmt.setString(5, armorSlotType.name());
                            invStmt.setInt(6, defencePoint);
                        } else {
                            invStmt.setNull(5, java.sql.Types.VARCHAR);
                            invStmt.setNull(6, java.sql.Types.INTEGER);
                        }

                        // Hvis vi har våben-info, sæt den – ellers indsæt NULL.
                        if (weaponSlotType != null) {
                            invStmt.setString(7, weaponSlotType.name());
                            invStmt.setInt(8, damage);
                        } else {
                            invStmt.setNull(7, java.sql.Types.VARCHAR);
                            invStmt.setNull(8, java.sql.Types.INTEGER);
                        }

                        // Stack size (1 for ikke-stackable, >1 for fx potions).
                        invStmt.setInt(9, stackSize);

                        // Tilføj denne række til batch, så vi kan indsætte flere på én gang.
                        invStmt.addBatch();
                    }

                    // Udfør alle insert-statements i én batch for bedre performance.
                    invStmt.executeBatch();
                }

                // Gemmer generel inventory-information i InventoryMisc_DB.
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

    // Loader spillet fra databasen og genskaber Inventory-tilstanden.
    @FXML
    private void loadGame() throws SQLException {
        // Bekræftelses-popup, da load overskriver nuværende fremgang.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to load the game? (Current progress will be lost)");

        Optional<ButtonType> result = alert.showAndWait();

        // Kun load hvis brugeren trykker OK.
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // Opretter databaseforbindelse.
            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                // Først læses de generelle inventory-værdier (guld, vægt, slots, upgrades).
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

                // Rydder nuværende items, så vi kan genindlæse fra databasen.
                inventory.clearItems();

                // Henter alle items der er gemt i Inventory_DB.
                String sqlInv = "SELECT itemName, stackSize " +
                        "FROM Inventory_DB";

                try (PreparedStatement invStmt = conn.prepareStatement(sqlInv);
                     ResultSet invRs = invStmt.executeQuery()) {

                    while (invRs.next()) {
                        String name = invRs.getString("itemName");
                        int stackSize = invRs.getInt("stackSize");

                        // Find ItemName-enummen ud fra det gemte navn.
                        ItemName itemNameEnum = ItemNameToItemNameRegistry.fromDisplayName(name);

                        // Opret et nyt item-objekt ud fra ItemRegistry-definitionen.
                        Item item = ItemRegistry.getDefinition(itemNameEnum).createInstance();

                        // Hvis item er stackable (Utility / Consumable), tilpas stackSize til det gemte.
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

                        // Tilføj item’et til inventory-listen.
                        inventory.addItemToInventory(item);
                    }
                }
            }

            // Opdater HUD, så den matcher de indlæste værdier.
            updateHud();
        }
    }

}
