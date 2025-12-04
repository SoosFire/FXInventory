package app.fxinventory.Inventory;

import java.util.ArrayList;

import app.fxinventory.Item.*;

public class Inventory {

    protected ArrayList<Item> inventory = new ArrayList<>();
    protected int gold = 0;
    protected double weight = 0;


    public void addItemToInventory(Item item) {
        inventory.add(item);
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
}
