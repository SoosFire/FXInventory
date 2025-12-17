package app.fxinventory.Item;

import app.fxinventory.Enums.*;

// Repræsenterer et forbrugs-item (fx potion, mad osv.), som kan stakkes.
// Arver fælles felter (name, weight, cost, type) fra Item.
public class Consumable extends Item {

    // Hvor mange enheder (stk) der er i denne stack.
    private int stackSize;

    // Konstruktør der opretter et nyt Consumable med navn, vægt, pris, type og start-stack.
    public Consumable(String name, double weight, int cost, ItemType type, int stackSize) {
        // Kalder superklassens (Item) konstruktør for at sætte de fælles værdier.
        super(name, weight, cost, type);
        this.stackSize = stackSize;
    }

    // Returnerer hvor mange enheder der er i denne stack.
    public int getStackSize() {
        return stackSize;
    }

    // Sætter stackSize direkte (bruges fx ved load fra database).
    public void setStackSize(int newStackSize) {
        this.stackSize = newStackSize;
    }

    // Justerer stackSize med et givent beløb (positivt = tilføj, negativt = fjern).
    public void updateStackSize(int amount) {
        this.stackSize += amount;
    }

    @Override
    public Item createInstance() {
        // Opretter en ny Consumable med samme værdier som denne.
        // Bruger de beskyttede felter (name, weight, cost, type) fra Item.
        return new Consumable(name, weight, cost, type, stackSize);
    }
}
