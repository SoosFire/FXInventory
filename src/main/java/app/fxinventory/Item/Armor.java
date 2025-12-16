package app.fxinventory.Item;

import app.fxinventory.Enums.*;

public class Armor extends Item {

    private int defencePoint;
    private final ArmorSlotType slotType;

    public Armor (String name, double weight, int cost, ItemType type, int defencePoint,  ArmorSlotType slotType) {
        super(name, weight, cost, type);
        this.defencePoint = defencePoint;
        this.slotType = slotType;
    }

    public int getDefencePoint() {
        return defencePoint;
    }
    public void updateDefencePoint(int amount) {
        this.defencePoint += amount;
    }
    public ArmorSlotType getSlotType() {
        return slotType;
    }

    @Override
    public Item createInstance(){
        return new Armor(name, weight,cost,type,defencePoint,slotType);
    }
}
