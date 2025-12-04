package app.fxinventory.Item;

import app.fxinventory.Enums.*;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private static final Map<ItemName, Item> DEFINITIONS = new HashMap<>();

    static {
        // Weapons
        DEFINITIONS.put(ItemName.Sword,
                new Weapon("Sword", 2.0, 100, 10,
                        ItemType.WEAPON, WeaponSlotType.ONE_HANDED, 1));

        DEFINITIONS.put(ItemName.DualSword,
                new Weapon("Dual Sword", 3.0, 150, 9,
                        ItemType.WEAPON, WeaponSlotType.DUAL_HANDED, 1));

        DEFINITIONS.put(ItemName.Axe,
                new Weapon("Axe", 10.0, 200, 20,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED, 1));

        DEFINITIONS.put(ItemName.Pickaxe,
                new Weapon("Pickaxe", 5.0, 120, 8,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED, 1));

        DEFINITIONS.put(ItemName.Shovel,
                new Weapon("Shovel", 4.0, 80, 6,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED, 1));

        DEFINITIONS.put(ItemName.Dagger,
                new Weapon("Dagger", 1.0, 60, 7,
                        ItemType.WEAPON, WeaponSlotType.ONE_HANDED, 1));

        DEFINITIONS.put(ItemName.Staff,
                new Weapon("Staff", 3.0, 140, 12,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED, 1));

        DEFINITIONS.put(ItemName.Bow,
                new Weapon("Bow", 2.5, 160, 11,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED, 1));

        DEFINITIONS.put(ItemName.Crossbow,
                new Weapon("Crossbow", 3.5, 220, 14,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED, 1));

        DEFINITIONS.put(ItemName.Lance,
                new Weapon("Lance", 5.5, 250, 18,
                        ItemType.WEAPON, WeaponSlotType.TWO_HANDED, 1));


// Armor – Chest
        DEFINITIONS.put(ItemName.SteelChestplate,
                new Armor("Steel Chestplate", 20.0, 500,
                        ItemType.ARMOR, 10, ArmorSlotType.CHESTPLATE, 1));

        DEFINITIONS.put(ItemName.Chainmail,
                new Armor("Chainmail", 15.0, 350,
                        ItemType.ARMOR, 8, ArmorSlotType.CHESTPLATE, 1));

        DEFINITIONS.put(ItemName.LeatherChestplate,
                new Armor("Leather Chestplate", 8.0, 150,
                        ItemType.ARMOR, 4, ArmorSlotType.CHESTPLATE, 1));


// Armor – Helmets
        DEFINITIONS.put(ItemName.SteelHelmet,
                new Armor("Steel Helmet", 5.0, 200,
                        ItemType.ARMOR, 6, ArmorSlotType.HELMET, 1));

        DEFINITIONS.put(ItemName.LeatherHelmet,
                new Armor("Leather Helmet", 2.0, 80,
                        ItemType.ARMOR, 2, ArmorSlotType.HELMET, 1));

        DEFINITIONS.put(ItemName.ChainmailHelmet,
                new Armor("Chainmail Helmet", 3.0, 140,
                        ItemType.ARMOR, 4, ArmorSlotType.HELMET, 1));


// Armor – Leggings
        DEFINITIONS.put(ItemName.SteelLeggings,
                new Armor("Steel Leggings", 12.0, 300,
                        ItemType.ARMOR, 7, ArmorSlotType.LEGGINGS, 1));

        DEFINITIONS.put(ItemName.LeatherLeggings,
                new Armor("Leather Leggings", 5.0, 120,
                        ItemType.ARMOR, 3, ArmorSlotType.LEGGINGS, 1));

        DEFINITIONS.put(ItemName.ChainmailLeggings,
                new Armor("Chainmail Leggings", 8.0, 220,
                        ItemType.ARMOR, 5, ArmorSlotType.LEGGINGS, 1));


// Armor – Boots
        DEFINITIONS.put(ItemName.SteelBoots,
                new Armor("Steel Boots", 6.0, 180,
                        ItemType.ARMOR, 4, ArmorSlotType.BOOTS, 1));

        DEFINITIONS.put(ItemName.LeatherBoots,
                new Armor("Leather Boots", 2.5, 80,
                        ItemType.ARMOR, 2, ArmorSlotType.BOOTS, 1));

        DEFINITIONS.put(ItemName.ChainmailBoots,
                new Armor("Chainmail Boots", 4.0, 140,
                        ItemType.ARMOR, 3, ArmorSlotType.BOOTS, 1));


// Armor – Gauntlets
        DEFINITIONS.put(ItemName.SteelGauntlet,
                new Armor("Steel Gauntlet", 3.0, 150,
                        ItemType.ARMOR, 3, ArmorSlotType.GAUNTLET, 1));

        DEFINITIONS.put(ItemName.LeatherGauntlet,
                new Armor("Leather Gauntlet", 1.5, 60,
                        ItemType.ARMOR, 1, ArmorSlotType.GAUNTLET, 1));

        DEFINITIONS.put(ItemName.ChainmailGauntlet,
                new Armor("Chainmail Gauntlet", 2.0, 100,
                        ItemType.ARMOR, 2, ArmorSlotType.GAUNTLET, 1));


// Armor – Shoulder pieces & Cloak
// NOTE: ArmorSlotType currently has no SHOULDER / CLOAK slot.
// I'm treating these as CHESTPLATE items — you can add extra enum values later if you want.
        DEFINITIONS.put(ItemName.SteelShoulderPiece,
                new Armor("Steel Shoulder Piece", 4.0, 160,
                        ItemType.ARMOR, 3, ArmorSlotType.CHESTPLATE, 1));

        DEFINITIONS.put(ItemName.LeatherShoulderPiece,
                new Armor("Leather Shoulder Piece", 2.0, 90,
                        ItemType.ARMOR, 2, ArmorSlotType.CHESTPLATE, 1));

        DEFINITIONS.put(ItemName.ChainmailShoulderPiece,
                new Armor("Chainmail Shoulder Piece", 3.0, 130,
                        ItemType.ARMOR, 2, ArmorSlotType.CHESTPLATE, 1));

        DEFINITIONS.put(ItemName.Cloak,
                new Armor("Cloak", 1.0, 110,
                        ItemType.ARMOR, 1, ArmorSlotType.CHESTPLATE, 1));

    }

    public static Item getDefinition(ItemName itemName) {
        return DEFINITIONS.get(itemName);
    }
}
