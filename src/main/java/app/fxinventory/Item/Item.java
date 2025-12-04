package app.fxinventory.Item;

import app.fxinventory.Enums.*;

public abstract class Item {

    protected String name;
    protected double weight;
    protected int cost;
    protected ItemType type;
    protected int totalAmount;

    public Item(String name, double weight, int cost, ItemType type, int totalAmount) {
        this.name = name;
        this.weight = weight;
        this.cost = cost;
        this.type = type;
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
    public int getCost() {
        return cost;
    }

    public ItemType getType() {
        return type;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
    public void updateTotalAmount(int amount) {
        this.totalAmount += amount;
    }

    public abstract Item createInstance();
}
