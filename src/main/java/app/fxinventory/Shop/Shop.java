package app.fxinventory.Shop;

import app.fxinventory.Enums.*;
import app.fxinventory.Item.*;
import app.fxinventory.Inventory.*;

import java.util.ArrayList;

// Repræsenterer butikkens "service-lag".
// Indeholder logikken for at købe og sælge items i forhold til spillerens Inventory.
public class Shop {

    // Potentiel liste over varer i shoppen (bruges ikke direkte i den nuværende løsning).
    ArrayList<Item> shop = new ArrayList<>();

    // Håndterer køb af et item ud fra et ItemName (enum) og spillerens Inventory.
    public void buyItem(Inventory inventory, ItemName itemName) {
        // Opretter en ny instans af varen, baseret på definitionen i ItemRegistry.
        Item newItem = ItemRegistry.getDefinition(itemName).createInstance();

        // Kun Utility og Consumable er stackable (kan være flere i samme slot).
        boolean isUtility = newItem instanceof Utility;
        boolean isConsumable = newItem instanceof Consumable;

        // Hvis det ikke er stackable (fx Weapon eller Armor):
        if (!isUtility && !isConsumable) {
            // Læg item direkte i inventory som et separat item.
            inventory.addItemToInventory(newItem);
            inventory.updateBuyItemGold(newItem.getCost());      // træk guld
            inventory.updateBuyItemWeight(newItem.getWeight());  // tilføj vægt
            inventory.updateCurrentSlotUsed(1);                  // ny stak → nyt slot i brug
            return;
        }

        // Maks antal enheder i én stack.
        final int MAX_STACK = 16;

        // 1) Først: prøv at finde en eksisterende stack af samme item, der ikke er fuld.
        for (Item invItem : inventory.getItems()) {

            // Navnet skal matche (samme item); alternativt kunne man matche på ItemName.
            if (!invItem.getName().equals(newItem.getName())) {
                continue;
            }

            // --- Utility case ---
            if (invItem instanceof Utility existingUtility && isUtility) {
                if (existingUtility.getStackSize() < MAX_STACK) {
                    // Der er plads i stacken → tilføj 1 til stack.
                    existingUtility.updateStackSize(1);
                    inventory.updateBuyItemGold(newItem.getCost());
                    inventory.updateBuyItemWeight(newItem.getWeight());
                    return;
                }
            }

            // --- Consumable case ---
            if (invItem instanceof Consumable existingConsumable && isConsumable) {
                if (existingConsumable.getStackSize() < MAX_STACK) {
                    // Der er plads i stacken → tilføj 1.
                    existingConsumable.updateStackSize(1);
                    inventory.updateBuyItemGold(newItem.getCost());
                    inventory.updateBuyItemWeight(newItem.getWeight());
                    return;
                }
            }
        }

        // 2) Hvis vi ikke fandt en ikke-fuld stack → start en ny stack (nyt slot i brug).
        inventory.addItemToInventory(newItem);
        inventory.updateBuyItemGold(newItem.getCost());
        inventory.updateBuyItemWeight(newItem.getWeight());
        inventory.updateCurrentSlotUsed(1);
    }

    // Håndterer salg af et item fra spillerens Inventory.
    public void sellItem(Inventory inventory, Item item) {

        // ---------- Stackable: Utility ----------
        if (item instanceof Utility utility) {
            int stack = utility.getStackSize();

            if (stack > 1) {
                // Hvis der er mere end 1 i stacken → sælg 1 enhed.
                utility.updateStackSize(-1);
                inventory.addGold(item.getCost());          // få guld for salget
                inventory.addWeight(-item.getWeight());     // item vejer ikke længere
            } else {
                // Hvis det var den sidste i stacken → fjern hele item’et.
                inventory.removeItem(item);                 // fjerner item og justerer guld/vægt
                inventory.updateCurrentSlotUsed(-1);        // ét slot mindre i brug
            }
            return;
        }

        // ---------- Stackable: Consumable ----------
        if (item instanceof Consumable consumable) {
            int stack = consumable.getStackSize();

            if (stack > 1) {
                // Sælg kun 1 enhed fra stacken.
                consumable.updateStackSize(-1);
                inventory.addGold(item.getCost());
                inventory.addWeight(-item.getWeight());
            } else {
                // Sidste enhed → fjern hele item’et.
                inventory.removeItem(item);
                inventory.updateCurrentSlotUsed(-1);
            }
            return;
        }

        // ---------- Non-stackable: Weapon / Armor / andre ----------
        // Våben, rustning osv. sælges altid som ét helt item.
        inventory.removeItem(item);          // fjerner item + justerer guld/vægt
        inventory.updateCurrentSlotUsed(-1); // ét slot mindre i brug
    }
}
