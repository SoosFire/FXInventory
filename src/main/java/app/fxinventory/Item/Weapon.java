package app.fxinventory.Item;

import app.fxinventory.Enums.*;

public class Weapon extends Item {
    private int damage;
    private WeaponSlotType slotType;

    public Weapon (String name, double weight, int cost, int damage, ItemType itemType, WeaponSlotType slotType, int totalAmount) {
        super(name, weight, cost, itemType, totalAmount);
        this.damage = damage;
        this.slotType = slotType;

    }

    public WeaponSlotType getSlotType () {
        return slotType;
    }

    public String getName () {
        return super.getName();
    }

    public int getDamage () {
        return damage;
    }

    @Override
    public Item createInstance () {
        return new Weapon(name,weight,cost,damage,type,slotType,totalAmount  );
    }

}
