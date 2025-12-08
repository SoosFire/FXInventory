package app.fxinventory.Item;

import app.fxinventory.Enums.ItemName;

import java.util.HashMap;
import java.util.Map;

public class ItemNameToItemNameRegistry {

    private static final Map<String, ItemName> NameConverter = new HashMap<>();

    static {
        // ======================
        // Weapons
        // ======================
        NameConverter.put("Wooden Sword",
                ItemName.Wooden_Sword);

        NameConverter.put("Silver Sword",
                ItemName.Silver_Sword);

        NameConverter.put("Golden Sword",
                ItemName.Golden_Sword);

        NameConverter.put("Silver Axe",
                ItemName.Silver_Axe);

        NameConverter.put("Golden Axe",
                ItemName.Golden_Axe);

        NameConverter.put("Silver Heavy Sword",
                ItemName.Silver_Heavy_Sword);

        NameConverter.put("Golden Heavy Sword",
                ItemName.Golden_Heavy_Sword);

        NameConverter.put("Spear",
                ItemName.Spear);

        NameConverter.put("Steel Spear",
                ItemName.Steel_Spear);

        NameConverter.put("Staff",
                ItemName.Staff);

        NameConverter.put("Upgraded Staff",
                ItemName.Upgraded_Staff);

        NameConverter.put("Hammer",
                ItemName.Hammer);

        NameConverter.put("Golden Hammer",
                ItemName.Golden_Hammer);

        NameConverter.put("Dual Axe",
                ItemName.Dual_Axe);

        NameConverter.put("Golden Dual Axe",
                ItemName.Golden_Dual_Axe);


        // ======================
        // Armor – Helmets
        // ======================
        NameConverter.put("Basic Helmet",
                ItemName.Basic_Helmet);

        NameConverter.put("Golden Helmet",
                ItemName.Golden_Helmet);


        // ======================
        // Armor – Chest
        // ======================
        NameConverter.put("Basic Chest",
                ItemName.Basic_Chest);

        NameConverter.put("Golden Chest",
                ItemName.Golden_Chest);


        // ======================
        // Armor – Shields
        // ======================
        NameConverter.put("Small Wooden Shield",
                ItemName.Small_Wooden_Shield);

        NameConverter.put("Small Silver Shield",
                ItemName.Small_Silver_Shield);

        NameConverter.put("Large Wooden Shield",
                ItemName.Large_Wooden_Shield);

        NameConverter.put("Large Silver Shield",
                ItemName.Large_Silver_Shield);


        // ======================
        // Utility – Scrolls and Tomes
        // ======================
        NameConverter.put("Scroll",
                ItemName.Scroll);

        NameConverter.put("Blue Tome",
                ItemName.Blue_Tome);

        NameConverter.put("Green Tome",
                ItemName.Green_Tome);

        NameConverter.put("Red Tome",
                ItemName.Red_Tome);

        NameConverter.put("Yellow Tome",
                ItemName.Yellow_Tome);


        // ======================
        // Consumables
        // ======================
        NameConverter.put("Health Potion",
                ItemName.Health_Potion);

        NameConverter.put("Mana Potion",
                ItemName.Mana_Potion);

        NameConverter.put("Strength Potion",
                ItemName.Strength_Potion);

        NameConverter.put("Defence Potion",
                ItemName.Defence_Potion);
    }

    public static ItemName fromDisplayName(String name) {
        return NameConverter.get(name);
    }
}
