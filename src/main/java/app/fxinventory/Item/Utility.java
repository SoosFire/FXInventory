package app.fxinventory.Item;

import app.fxinventory.Enums.*;

public class Utility extends Item {
    private int stackSize;

    public Utility(String name, double weight, int cost, ItemType type, int stackSize, int totalAmount) {
        super(name, weight, cost, type, totalAmount);
        this.stackSize = stackSize;
    }

    public int getStackSize() {
        return stackSize;
    }
    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }
    public void updateStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    @Override
    public Item createInstance() {
        return new Utility(name, weight, cost, type, stackSize, totalAmount);
    }
}
