package app.fxinventory.Item;

import app.fxinventory.Enums.*;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private static final Map<ItemName, Item> DEFINITIONS = new HashMap<>();

    static {
        // ======================
        // Weapons
        // ======================
        DEFINITIONS.put(ItemName.Wooden_Sword,
                new Weapon("Wooden Sword", 2.0, 50, 6,
                        ItemType.WEAPON, WeaponSlotType.ONE_HANDED));

        DEFINITIONS.put(ItemName.Silver_Sword,
                new Weapon("Silver Sword", 2.2, 150, 10,
                        ItemType.WEAPON, WeaponSlotType.ONE_HANDED));

        DEFINITIONS.put(ItemName.Golden_Sword,
                new Weapon("Golden Sword", 2.4, 250, 14,
                        ItemType.WEAPON, WeaponSlotType.ONE_HANDED));

        DEFINITIONS.put(ItemName.Silver_Axe,
                new Weapon("Silver Axe", 4.0, 200, 16,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Golden_Axe,
                new Weapon("Golden Axe", 4.5, 280, 20,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Silver_Heavy_Sword,
                new Weapon("Silver Heavy Sword", 5.0, 260, 22,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Golden_Heavy_Sword,
                new Weapon("Golden Heavy Sword", 5.5, 340, 26,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Spear,
                new Weapon("Spear", 3.0, 120, 11,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Steel_Spear,
                new Weapon("Steel Spear", 3.5, 200, 15,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Staff,
                new Weapon("Staff", 3.0, 140, 12,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Upgraded_Staff,
                new Weapon("Upgraded Staff", 3.2, 220, 18,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Hammer,
                new Weapon("Hammer", 4.5, 220, 18,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Golden_Hammer,
                new Weapon("Golden Hammer", 5.0, 320, 24,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED));

        DEFINITIONS.put(ItemName.Dual_Axe,
                new Weapon("Dual Axe", 4.0, 260, 17,
                        ItemType.WEAPON, WeaponSlotType.DUAL_HANDED));

        DEFINITIONS.put(ItemName.Golden_Dual_Axe,
                new Weapon("Golden Dual Axe", 4.4, 340, 22,
                        ItemType.WEAPON, WeaponSlotType.DUAL_HANDED));


        // ======================
        // Armor – Helmets
        // ======================
        DEFINITIONS.put(ItemName.Basic_Helmet,
                new Armor("Basic Helmet", 3.0, 100,
                        ItemType.ARMOR, 3, ArmorSlotType.HELMET));

        DEFINITIONS.put(ItemName.Golden_Helmet,
                new Armor("Golden Helmet", 3.5, 260,
                        ItemType.ARMOR, 6, ArmorSlotType.HELMET));


        // ======================
        // Armor – Chest
        // ======================
        DEFINITIONS.put(ItemName.Basic_Chest,
                new Armor("Basic Chest", 10.0, 300,
                        ItemType.ARMOR, 8, ArmorSlotType.CHESTPLATE));

        DEFINITIONS.put(ItemName.Golden_Chest,
                new Armor("Golden Chest", 11.0, 500,
                        ItemType.ARMOR, 12, ArmorSlotType.CHESTPLATE));


        // ======================
        // Armor – Shields
        // (if your ArmorSlotType name is different, change SHIELD accordingly)
        // ======================
        DEFINITIONS.put(ItemName.Small_Wooden_Shield,
                new Armor("Small Wooden Shield", 4.0, 120,
                        ItemType.ARMOR, 4, ArmorSlotType.SHIELD));

        DEFINITIONS.put(ItemName.Small_Silver_Shield,
                new Armor("Small Silver Shield", 4.5, 220,
                        ItemType.ARMOR, 7, ArmorSlotType.SHIELD));

        DEFINITIONS.put(ItemName.Large_Wooden_Shield,
                new Armor("Large Wooden Shield", 6.0, 200,
                        ItemType.ARMOR, 7, ArmorSlotType.SHIELD));

        DEFINITIONS.put(ItemName.Large_Silver_Shield,
                new Armor("Large Silver Shield", 6.5, 340,
                        ItemType.ARMOR, 10, ArmorSlotType.SHIELD));

        // ======================
        // Utility – Scrolls and Tomes
        // ======================
        DEFINITIONS.put(ItemName.Scroll,
                new Utility("Scroll", 0.5, 80,
                        ItemType.UTILITY, 1));

        DEFINITIONS.put(ItemName.Blue_Tome,
                new Utility("Blue Tome", 1.0, 150,
                        ItemType.UTILITY, 1));

        DEFINITIONS.put(ItemName.Green_Tome,
                new Utility("Green Tome", 1.0, 150,
                        ItemType.UTILITY, 1));

        DEFINITIONS.put(ItemName.Red_Tome,
                new Utility("Red Tome", 1.0, 150,
                        ItemType.UTILITY, 1));

        DEFINITIONS.put(ItemName.Yellow_Tome,
                new Utility("Yellow Tome", 1.0, 150,
                        ItemType.UTILITY, 1));


        // ======================
        // Consumables
        // ======================
        DEFINITIONS.put(ItemName.Health_Potion,
                new Consumable("Health Potion", 1.0, 100,
                        ItemType.CONSUMABLE, 1));

        DEFINITIONS.put(ItemName.Mana_Potion,
                new Consumable("Mana Potion", 1.0, 100,
                        ItemType.CONSUMABLE, 1));

        DEFINITIONS.put(ItemName.Strength_Potion,
                new Consumable("Strength Potion", 1.0, 120,
                        ItemType.CONSUMABLE, 1));

        DEFINITIONS.put(ItemName.Defence_Potion,
                new Consumable("Defence Potion", 1.0, 120,
                        ItemType.CONSUMABLE, 1));
    }

    public static Item getDefinition(ItemName itemName) {
        return DEFINITIONS.get(itemName);
    }
}
