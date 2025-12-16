package app.fxinventory.Inventory;

import app.fxinventory.Item.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    // Inventory attributter

    protected ArrayList<Item> inventory = new ArrayList<>();
    protected int gold = 11500;                 // Specificerer [gold] start amount
    protected double weight = 0;                // Specificerer [weight] start amount
    protected int weightLimit = 50;             // Specificerer [weightLimit] - Hvor meget plads kan vi maks have?
    protected int slotLimit = 192;
    protected int availableSlots = 32;
    protected int currentSlotUsed;

    private int level_SlotUpgradeOne = 1;
    private int level_SlotUpgradeTwo = 1;
    private int level_SlotUpgradeThree = 1;


    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<Item> getItems() {
        return inventory;
    }

    public int getGold() {
        return gold;
    }

    public double getWeight() {
        return weight;
    }

    public void addWeight(double weightAmount) {
        this.weight += weightAmount;
    }

    public void addGold(int goldAmount) {
        this.gold += goldAmount;
    }

    public void removeItem(Item item) {
        inventory.remove(item);
        gold += item.getCost();
        weight -= item.getWeight();
    }

    public void updateBuyItemGold(int itemCost) {
        this.gold -= itemCost;
    }
    public void updateBuyItemWeight(double itemWeight) {
        this.weight += itemWeight;
    }

    public int getWeightLimit(){
        return  weightLimit;
    }
    public void updateWeightLimit(int amount){
        weightLimit += amount;
    }

    public int getSlotLimit(){
        return  slotLimit;
    }
    public int getAvailableSlots(){
        return availableSlots;
    }
    public void updateAvailableSlots(int amount){
        availableSlots += amount;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void setCurrentSlotUsed(int slotUsed) {
        this.currentSlotUsed = slotUsed;
    }
    public void updateCurrentSlotUsed(int amount){
        currentSlotUsed += amount;
    }

    public int getCurrentSlotUsed(){
        return currentSlotUsed;
    }

    public int getSlotUpgradeOne(){
        return level_SlotUpgradeOne;
    }
    public int getSlotUpgradeTwo(){
        return level_SlotUpgradeTwo;
    }
    public int getSlotUpgradeThree(){
        return level_SlotUpgradeThree;
    }

    public void setSlotUpgradeOne(int slot){
        this.level_SlotUpgradeOne = slot;
    }
    public void setSlotUpgradeTwo(int slot){
        this.level_SlotUpgradeTwo = slot;
    }
    public void setSlotUpgradeThree(int slot){
        this.level_SlotUpgradeThree = slot;
    }
    public void clearItems(){
        inventory.clear();
    }

}
