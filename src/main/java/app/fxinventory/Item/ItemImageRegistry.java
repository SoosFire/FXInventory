package app.fxinventory.Item;

import app.fxinventory.Enums.ItemName;

import java.util.HashMap;
import java.util.Map;

public class ItemImageRegistry {

    private static final Map<ItemName, String> IMAGE_PATH = new HashMap<>();

    static {
        // ======================
        // Weapons
        // ======================
        IMAGE_PATH.put(ItemName.Wooden_Sword,
                "/app/fxinventory/Images/SwordWood.png");
        IMAGE_PATH.put(ItemName.Silver_Sword,
                "/app/fxinventory/Images/DaggerT1.png");
        IMAGE_PATH.put(ItemName.Golden_Sword,
                "/app/fxinventory/Images/DaggerT2.png");

        IMAGE_PATH.put(ItemName.Silver_Axe,
                "/app/fxinventory/Images/AxeT1.png");
        IMAGE_PATH.put(ItemName.Golden_Axe,
                "/app/fxinventory/Images/AxeT2.png");

        IMAGE_PATH.put(ItemName.Silver_Heavy_Sword,
                "/app/fxinventory/Images/SwordT1.png");
        IMAGE_PATH.put(ItemName.Golden_Heavy_Sword,
                "/app/fxinventory/Images/SwordT2.png");

        IMAGE_PATH.put(ItemName.Spear,
                "/app/fxinventory/Images/SpearT1.png");
        IMAGE_PATH.put(ItemName.Steel_Spear,
                "/app/fxinventory/Images/SpearT2.png");

        IMAGE_PATH.put(ItemName.Staff,
                "/app/fxinventory/Images/WandT1.png");
        IMAGE_PATH.put(ItemName.Upgraded_Staff,
                "/app/fxinventory/Images/WandT2.png");

        IMAGE_PATH.put(ItemName.Hammer,
                "/app/fxinventory/Images/HammerT1.png");
        IMAGE_PATH.put(ItemName.Golden_Hammer,
                "/app/fxinventory/Images/HammerT2.png");

        IMAGE_PATH.put(ItemName.Dual_Axe,
                "/app/fxinventory/Images/AxeDoubleT1.png");
        IMAGE_PATH.put(ItemName.Golden_Dual_Axe,
                "/app/fxinventory/Images/AxeDoubleT2.png");


        // ======================
        // Armor – Helmets
        // ======================
        IMAGE_PATH.put(ItemName.Basic_Helmet,
                "/app/fxinventory/Images/HelmetT1.png");
        IMAGE_PATH.put(ItemName.Golden_Helmet,
                "/app/fxinventory/Images/HelmetT2.png");


        // ======================
        // Armor – Chest
        // ======================
        IMAGE_PATH.put(ItemName.Basic_Chest,
                "/app/fxinventory/Images/ArmorT1.png");
        IMAGE_PATH.put(ItemName.Golden_Chest,
                "/app/fxinventory/Images/ArmorT2.png");


        // ======================
        // Armor – Shields
        // ======================
        IMAGE_PATH.put(ItemName.Small_Wooden_Shield,
                "/app/fxinventory/Images/ShieldSmallT1.png");
        IMAGE_PATH.put(ItemName.Small_Silver_Shield,
                "/app/fxinventory/Images/ShieldSmallT2.png");
        IMAGE_PATH.put(ItemName.Large_Wooden_Shield,
                "/app/fxinventory/Images/ShieldLargeT1.png");
        IMAGE_PATH.put(ItemName.Large_Silver_Shield,
                "/app/fxinventory/Images/ShieldLargeT2.png");

        // ======================
        // Utilities – Scroll & Tomes
        // ======================
        IMAGE_PATH.put(ItemName.Scroll,
                "/app/fxinventory/Images/Scroll.png");
        IMAGE_PATH.put(ItemName.Blue_Tome,
                "/app/fxinventory/Images/TomeBlue.png");
        IMAGE_PATH.put(ItemName.Green_Tome,
                "/app/fxinventory/Images/TomeGreen.png");
        IMAGE_PATH.put(ItemName.Red_Tome,
                "/app/fxinventory/Images/TomeRed.png");
        IMAGE_PATH.put(ItemName.Yellow_Tome,
                "/app/fxinventory/Images/TomeYellow.png");


        // ======================
        // Consumables
        // ======================
        IMAGE_PATH.put(ItemName.Health_Potion,
                "/app/fxinventory/Images/PotionRed.png");
        IMAGE_PATH.put(ItemName.Mana_Potion,
                "/app/fxinventory/Images/PotionBlue.png");
        IMAGE_PATH.put(ItemName.Strength_Potion,
                "/app/fxinventory/Images/PotionYellow.png");
        IMAGE_PATH.put(ItemName.Defence_Potion,
                "/app/fxinventory/Images/PotionGreen.png");
    }

    public static String getDefinition(ItemName itemName) {
        return IMAGE_PATH.get(itemName);
    }
}
