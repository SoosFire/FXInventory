package app.fxinventory.Item;

import app.fxinventory.Enums.*;

// Abstrakt base-klasse for alle items i spillet.
// Fælles egenskaber (navn, vægt, pris og type) ligger her,
// mens specifik logik ligger i subklasser som Weapon, Armor, Utility og Consumable.
public abstract class Item {

    // Det navn, som item’et vises med i spillet/GUI.
    protected String name;

    // Hvor meget item’et vejer (bruges til vægtbegrænsning).
    protected double weight;

    // Hvor meget item’et koster i guld.
    protected int cost;

    // Overordnet type for item’et (WEAPON, ARMOR, UTILITY, CONSUMABLE).
    protected ItemType type;

    // Konstruktør der sætter de fælles felter for alle items.
    public Item(String name, double weight, int cost, ItemType type) {
        this.name = name;
        this.weight = weight;
        this.cost = cost;
        this.type = type;
    }

    // Getter til navn på item.
    public String getName() {
        return name;
    }

    // Getter til vægt på item.
    public double getWeight() {
        return weight;
    }

    // Getter til pris på item.
    public int getCost() {
        return cost;
    }

    // Getter til itemtype (ItemType-enum).
    public ItemType getType() {
        return type;
    }

    // Metode der er tænkt til at blive overridet af subklasser,
    // hvis de har behov for at opdatere en form for "total amount" (fx stackSize).
    // I denne base-klasse gør den ikke noget.
    public void updateTotalAmount(int amount) {
    }

    // Abstrakt metode som alle subklasser skal implementere.
    // Returnerer en ny instans af den konkrete item-type med samme værdier,
    // og bruges bl.a. af ItemRegistry til at "klone" items.
    public abstract Item createInstance();
}
