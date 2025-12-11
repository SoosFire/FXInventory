package app.fxinventory.Inventory;

import app.fxinventory.Item.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    // Inventory attributter

    protected ArrayList<Item> inventory = new ArrayList<>();
    protected int gold = 11500;                 // Specificerer [gold] start amount
    protected double weight = 0;                // Specificerer [weight] start amount
    protected int weightLimit = 50;        // Specificerer [weightLimit] - Hvor meget plads kan vi maks have?

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public List<Item> getItems() {
        return inventory;
    }

    public int getGold() {
        return gold;
    }

    public double getWeight() {
        return weight;
    }

    public void addWeight(double weightAmount) {
        this.weight += weightAmount;
    }

    public void addGold(int goldAmount) {
        this.gold += goldAmount;
    }

    public void removeItem(Item item) {
        inventory.remove(item);
        gold += item.getCost();
        weight -= item.getWeight();
    }

    public void updateBuyItemGold(int itemCost) {
        this.gold -= itemCost;
    }
    public void updateBuyItemWeight(double itemWeight) {
        this.weight += itemWeight;
    }

    public int getWeightLimit(){
        return  weightLimit;
    }
}
