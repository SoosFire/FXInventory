package app.fxinventory.Shop;

import app.fxinventory.Enums.*;
import app.fxinventory.Item.*;
import app.fxinventory.Inventory.*;

import java.util.ArrayList;

public class Shop {

    ArrayList<Item> shop = new ArrayList<>();

    public void buyItem(Inventory inventory, ItemName itemName) {
        Item boughtItem = ItemRegistry.getDefinition(itemName).createInstance();
        inventory.addItemToInventory(boughtItem);
    }

    public void sellItem(Inventory inventory, Item item) {
        inventory.removeItem(item);
        int sellGoldAmount = item.getCost();
        inventory.addGold(sellGoldAmount);
    }
}
