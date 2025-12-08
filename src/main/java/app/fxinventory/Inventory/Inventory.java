package app.fxinventory.Inventory;

import app.fxinventory.Item.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    protected ArrayList<Item> inventory = new ArrayList<>();
    protected int gold = 8000;
    protected double weight = 0;

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public List<Item> getItems() {
        return inventory;
    }

    public String showInventory() {
        String result = "";
        for (int i = 0; i < inventory.size(); i++) {
            result += String.format("%s\n", inventory.get(i).getTotalAmount() + "x " + inventory.get(i).getName());
        }
        return result;
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
}
