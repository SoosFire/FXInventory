package app.fxinventory.Item;

import app.fxinventory.Enums.*;

// Repræsenterer et "utility"-item, fx ressourcer, materialer eller andre genstande,
// som kan stakkes i inventory (flere af samme type i én slot).
// Arver fælles felter (name, weight, cost, type) fra Item.
public class Utility extends Item {

    // Antal enheder i denne stack (fx 8 x "Iron Ore").
    private int stackSize;

    // Konstruktør der opretter et nyt Utility-item med navn, vægt, pris, type og stackSize.
    public Utility(String name, double weight, int cost, ItemType type, int stackSize) {
        // Kalder superklassens (Item) konstruktør for at sætte basale værdier.
        super(name, weight, cost, type);
        this.stackSize = stackSize;
    }

    // Returnerer hvor mange enheder der er i denne stack.
    public int getStackSize() {
        return stackSize;
    }

    // Sætter stackSize direkte (bruges fx ved load fra database).
    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    // Justerer stackSize med et givent beløb (positivt = tilføj, negativt = fjern).
    public void updateStackSize(int amount) {
        this.stackSize += amount;
    }

    @Override
    public Item createInstance() {
        // Opretter en ny Utility med samme værdier som denne instans.
        // Bruger de beskyttede felter (name, weight, cost, type) fra Item.
        return new Utility(name, weight, cost, type, stackSize);
    }
}
