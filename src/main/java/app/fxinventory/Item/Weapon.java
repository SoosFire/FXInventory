package app.fxinventory.Item;

import app.fxinventory.Enums.*;

public class Weapon extends Item {
    private int damage;
    private WeaponSlotType slotType;

    // Oprettelse af ny Weapons instans med given attributter
    public Weapon (String name, double weight, int cost, int damage, ItemType itemType, WeaponSlotType slotType, int totalAmount) {
        super(name, weight, cost, itemType, totalAmount);
        this.damage = damage;
        this.slotType = slotType;

    }

    // Returnerer slotType
    public WeaponSlotType getSlotType () {
        return slotType;
    }

    //Returnerer navn
    public String getName () {
        return super.getName();
    }

    // Returnerer damage
    public int getDamage () {
        return damage;
    }

    // Laver en ny instans af Weapon.
    @Override
    public Item createInstance () {
        return new Weapon(name,weight,cost,damage,type,slotType,totalAmount  );
    }

}
