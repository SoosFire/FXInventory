package app.fxinventory.Shop;

import app.fxinventory.Enums.*;
import app.fxinventory.Item.*;
import app.fxinventory.Inventory.*;

import java.util.ArrayList;

public class Shop {

    ArrayList<Item> shop = new ArrayList<>();

    public void buyItem(Inventory inventory, ItemName itemName) {
        // New item instance for this purchase
        Item newItem = ItemRegistry.getDefinition(itemName).createInstance();

        // Only Utility and Consumable are stackable
        boolean isUtility = newItem instanceof Utility;
        boolean isConsumable = newItem instanceof Consumable;

        if (!isUtility && !isConsumable) {
            // Non-stackable: just add as a separate item
            inventory.addItemToInventory(newItem);
            inventory.updateBuyItemGold(newItem.getCost());
            inventory.updateBuyItemWeight(newItem.getWeight());
            return;
        }

        final int MAX_STACK = 16;

        // 1) Try to add to an existing stack of the same item
        for (Item invItem : inventory.getItems()) {

            // Must be same display name (or better: same ItemName if you store it)
            if (!invItem.getName().equals(newItem.getName())) {
                continue;
            }

            // --- Utility case ---
            if (invItem instanceof Utility existingUtility && isUtility) {
                if (existingUtility.getStackSize() < MAX_STACK) {
                    existingUtility.updateStackSize(1);   // increment stack
                    inventory.updateBuyItemGold(newItem.getCost());
                    inventory.updateBuyItemWeight(newItem.getWeight());
                    return;
                }
            }

            // --- Consumable case ---
            if (invItem instanceof Consumable existingConsumable && isConsumable) {
                if (existingConsumable.getStackSize() < MAX_STACK) {
                    existingConsumable.updateStackSize(1);  // increment stack
                    inventory.updateBuyItemGold(newItem.getCost());
                    inventory.updateBuyItemWeight(newItem.getWeight());
                    return;
                }
            }
        }

        // 2) No non-full stack found → start a new stack
        inventory.addItemToInventory(newItem);
        inventory.updateBuyItemGold(newItem.getCost());
        inventory.updateBuyItemWeight(newItem.getWeight());
    }

    public void sellItem(Inventory inventory, Item item) {
            // ---------- Stackable: Utility ----------
            if (item instanceof Utility utility) {
                int stack = utility.getStackSize();   // or getTotalAmount(), adjust to your API

                if (stack > 1) {
                    // sell one from the stack
                    utility.updateStackSize(-1);      // decrement stack by 1
                    inventory.addGold(item.getCost());
                    inventory.addWeight(-item.getWeight());
                } else {
                    // last one -> remove item entirely (and get full value)
                    inventory.removeItem(item);       // your Inventory.removeItem already:
                    //  gold += cost, weight -= weight
                }
                return;
            }

            // ---------- Stackable: Consumable ----------
            if (item instanceof Consumable consumable) {
                int stack = consumable.getStackSize(); // or getTotalAmount()

                if (stack > 1) {
                    consumable.updateStackSize(-1);
                    inventory.addGold(item.getCost());
                    inventory.addWeight(-item.getWeight());
                } else {
                    inventory.removeItem(item);
                }
                return;
            }

            // ---------- Non-stackable: Weapon / Armor / other ----------
            // For Weapon/Armor you just sell/remove the whole item:
            inventory.removeItem(item);
    }
}
