package app.fxinventory.Item;

import app.fxinventory.Enums.*;

// Repræsenterer et våben i spillet (fx sværd, økse osv.).
// Arver fælles felter (name, weight, cost, type) fra Item.
public class Weapon extends Item {

    // Hvor meget skade våbnet giver.
    private int damage;

    // Hvilken type våben-slot det bruger (ONE_HANDED, TWO_HANDED, DUAL_HANDED).
    private WeaponSlotType slotType;

    // Opretter en ny Weapon-instans med de angivne værdier.
    // itemType er typisk ItemType.WEAPON.
    public Weapon (String name, double weight, int cost, int damage, ItemType itemType, WeaponSlotType slotType) {
        // Kalder superklassens (Item) konstruktør for at sætte name, weight, cost og type.
        super(name, weight, cost, itemType);
        this.damage = damage;
        this.slotType = slotType;
    }

    // Returnerer våbnets slot-type (fx ONE_HANDED).
    public WeaponSlotType getSlotType () {
        return slotType;
    }

    // Returnerer våbnets navn.
    // Denne metode overflødiggør egentlig ikke super.getName(), men er fin for læsbarhed.
    public String getName () {
        return super.getName();
    }

    // Returnerer hvor meget skade våbnet giver.
    public int getDamage () {
        return damage;
    }

    // Opretter en ny instans af Weapon med de samme værdier som dette objekt.
    // Bruges bl.a. af ItemRegistry til at "klone" våben ud fra en skabelon.
    @Override
    public Item createInstance () {
        return new Weapon(name, weight, cost, damage, type, slotType);
    }

}
