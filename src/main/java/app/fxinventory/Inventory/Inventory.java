package app.fxinventory.Inventory;

import app.fxinventory.Item.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    // Liste over alle items i spillerens inventory.
    protected ArrayList<Item> inventory = new ArrayList<>();

    // Spillerens nuværende guld.
    protected int gold = 11500;

    // Den samlede vægt af alle items i inventory.
    protected double weight = 0;

    // Maksimal vægt, spilleren må bære.
    protected int weightLimit = 50;

    // Teoretisk maks antal slots (bruges som øvre grænse, hvis du vil udvide).
    protected int slotLimit = 192;

    // Antal slots, spilleren har låst op (kan øges via upgrades).
    protected int availableSlots = 32;

    // Hvor mange slots der lige nu er i brug (antal “stakke”/items).
    protected int currentSlotUsed;

    // Level for de tre slot-upgrades (1 = ikke købt, 2 = købt/aktiv).
    private int level_SlotUpgradeOne = 1;
    private int level_SlotUpgradeTwo = 1;
    private int level_SlotUpgradeThree = 1;

    // -----------------------------------------
    // Item-håndtering
    // -----------------------------------------

    // Tilføjer et item til inventory-listen.
    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    // Returnerer en liste med alle items i inventory.
    public List<Item> getItems() {
        return inventory;
    }

    // Fjerner et item fra inventory og justerer guld og vægt.
    // Bemærk: denne metode både fjerner og "refunderer" værdien i guld.
    public void removeItem(Item item) {
        inventory.remove(item);
        gold += item.getCost();
        weight -= item.getWeight();
    }

    // Rydder hele inventory-listen (alle items fjernes).
    public void clearItems(){
        inventory.clear();
    }

    // -----------------------------------------
    // Guld (gold)
    // -----------------------------------------

    // Sætter spillerens guld til en specifik værdi.
    public void setGold(int gold) {
        this.gold = gold;
    }

    // Returnerer nuværende mængde guld.
    public int getGold() {
        return gold;
    }

    // Lægger et beløb til guld (kan være negativt for at trække fra).
    public void addGold(int goldAmount) {
        this.gold += goldAmount;
    }

    // Bruges hvis du vil trække prisen for et item én-til-én.
    // (Funktionelt samme idé som addGold(-itemCost)).
    public void updateBuyItemGold(int itemCost) {
        this.gold -= itemCost;
    }

    // -----------------------------------------
    // Vægt (weight)
    // -----------------------------------------

    // Sætter den totale vægt direkte (bruges fx ved load fra database).
    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Returnerer den samlede vægt af inventory.
    public double getWeight() {
        return weight;
    }

    // Justerer vægten med et givent beløb (positivt eller negativt).
    public void addWeight(double weightAmount) {
        this.weight += weightAmount;
    }

    // Bruges hvis du vil lægge vægten for et item til separat.
    public void updateBuyItemWeight(double itemWeight) {
        this.weight += itemWeight;
    }

    // Returnerer den maksimale vægtgrænse.
    public int getWeightLimit(){
        return  weightLimit;
    }

    // Øger vægtgrænsen (hvis du ønsker upgrades der påvirker weight).
    public void updateWeightLimit(int amount){
        weightLimit += amount;
    }

    // -----------------------------------------
    // Slots (plads i inventory)
    // -----------------------------------------

    // Returnerer hvor mange slots spilleren har til rådighed.
    public int getAvailableSlots(){
        return availableSlots;
    }

    // Øger (eller sænker) antallet af tilgængelige slots.
    public void updateAvailableSlots(int amount){
        availableSlots += amount;
    }

    // Sætter antal tilgængelige slots direkte (bruges fx ved load fra DB).
    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    // Sætter hvor mange slots der er i brug (fx ved load).
    public void setCurrentSlotUsed(int slotUsed) {
        this.currentSlotUsed = slotUsed;
    }

    // Justerer slots i brug med et givent beløb (fx +1 når man tilføjer en ny stak).
    public void updateCurrentSlotUsed(int amount){
        currentSlotUsed += amount;
    }

    // Returnerer hvor mange slots der p.t. er i brug.
    public int getCurrentSlotUsed(){
        return currentSlotUsed;
    }

    // -----------------------------------------
    // Slot-upgrades (level for opgraderinger)
    // -----------------------------------------

    // Henter level for slot-upgrade 1.
    public int getSlotUpgradeOne(){
        return level_SlotUpgradeOne;
    }

    // Henter level for slot-upgrade 2.
    public int getSlotUpgradeTwo(){
        return level_SlotUpgradeTwo;
    }

    // Henter level for slot-upgrade 3.
    public int getSlotUpgradeThree(){
        return level_SlotUpgradeThree;
    }

    // Sætter level for slot-upgrade 1 (bruges fra Upgrades_Controller).
    public void setSlotUpgradeOne(int slot){
        this.level_SlotUpgradeOne = slot;
    }

    // Sætter level for slot-upgrade 2.
    public void setSlotUpgradeTwo(int slot){
        this.level_SlotUpgradeTwo = slot;
    }

    // Sætter level for slot-upgrade 3.
    public void setSlotUpgradeThree(int slot){
        this.level_SlotUpgradeThree = slot;
    }
}
