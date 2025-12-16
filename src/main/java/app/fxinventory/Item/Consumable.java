package app.fxinventory.Item;

import app.fxinventory.Enums.*;

public class Consumable extends Item{

    private int stackSize;

    public Consumable(String name, double weight, int cost, ItemType type, int stackSize) {
        super(name, weight, cost, type);
        this.stackSize = stackSize;
    }

    public int getStackSize() {
        return stackSize;
    }
    public void setStackSize(int newStackSize) {
        this.stackSize = newStackSize;
    }
    public void updateStackSize(int amount) {
        this.stackSize += amount;
    }

    @Override
    public Item createInstance() {
        return new Consumable(name,weight,cost,type,stackSize);
    }
}
