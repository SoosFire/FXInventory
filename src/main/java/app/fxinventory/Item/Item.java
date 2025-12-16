package app.fxinventory.Item;

import app.fxinventory.Enums.*;

public abstract class Item {

    protected String name;
    protected double weight;
    protected int cost;
    protected ItemType type;

    public Item(String name, double weight, int cost, ItemType type) {
        this.name = name;
        this.weight = weight;
        this.cost = cost;
        this.type = type;
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

    public void updateTotalAmount(int amount) {
    }

    public abstract Item createInstance();
}
