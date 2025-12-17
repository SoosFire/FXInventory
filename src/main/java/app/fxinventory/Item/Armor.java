package app.fxinventory.Item;

import app.fxinventory.Enums.*;

// Repræsenterer et stykke rustning (armor) i spillet.
// Arver fælles egenskaber (name, weight, cost, type) fra Item.
public class Armor extends Item {

    // Hvor mange defence points denne rustning giver spilleren.
    private int defencePoint;

    // Hvilken "slot" på kroppen rustningen hører til (fx HEAD, CHEST osv.).
    private final ArmorSlotType slotType;

    // Konstruktør der opretter en ny Armor med alle nødvendige værdier.
    public Armor (String name, double weight, int cost, ItemType type, int defencePoint, ArmorSlotType slotType) {
        // Kalder superklassens (Item) konstruktør for at sætte fælles felter.
        super(name, weight, cost, type);
        this.defencePoint = defencePoint;
        this.slotType = slotType;
    }

    // Returnerer hvor meget defence rustningen giver.
    public int getDefencePoint() {
        return defencePoint;
    }

    // Justerer defencePoint med et givent beløb (positivt eller negativt).
    public void updateDefencePoint(int amount) {
        this.defencePoint += amount;
    }

    // Returnerer hvilken armor-slot denne rustning passer i (HEAD, CHEST, osv.).
    public ArmorSlotType getSlotType() {
        return slotType;
    }

    @Override
    public Item createInstance(){
        // Opretter en ny Armor med samme værdier som denne.
        // Bruger de beskyttede felter (name, weight, cost, type) fra Item.
        return new Armor(name, weight, cost, type, defencePoint, slotType);
    }
}
