package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the inventory of a player in the game.
 * It manages the items the player has collected and their coin balance.
 */
public class Inventory implements Serializable {
    private static final Logger logger = Logger.getLogger(Inventory.class.getName());

    private int capacity; // The maximum capacity of the inventory
    private List<GameObject> items; // The list of items in the inventory
    private int coinBalance; // The amount of coins the player has

    /**
     * Constructs an inventory with the specified capacity.
     *
     * @param capacity The maximum capacity of the inventory.
     */
    public Inventory(int capacity) {
        this.capacity = capacity;
        this.coinBalance = 0;
        this.items = new ArrayList<>();
    }

    /**
     * Retrieves the current coin balance of the player.
     *
     * @return The coin balance of the player.
     */
    public int getCoinBalance() {
        return this.coinBalance;
    }

    /**
     * Adds coins to the player's coin balance.
     *
     * @param amount The amount of coins to add.
     */
    public void addCoinToBalance(int amount) {
        this.coinBalance += amount;
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item The item to add to the inventory.
     */
    public void addItem(GameObject item) {
        if (items.size() < capacity) {
            items.add(item);
        } else {
            logger.log(Level.WARNING, "Inventory is full");
        }
    }

    /**
     * Saves the current inventory to a file.
     *
     * @param fileName The name of the file to save the inventory to.
     */
    public void saveInventory(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            logger.log(Level.INFO, "Inventory saved successfully.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving inventory: " + e.getMessage(), e);
        }
    }

    /**
     * Loads an inventory from a file.
     *
     * @param fileName The name of the file to load the inventory from.
     * @return The loaded inventory, or null if loading fails.
     */
    public static Inventory loadInventory(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Inventory inventory = (Inventory) inputStream.readObject();
            logger.log(Level.INFO, "Inventory loaded successfully.");
            return inventory;
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error loading inventory: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item The item to remove from the inventory.
     */
    public void removeItem(GameObject item) {
        items.remove(item);
    }

    /**
     * Removes multiple instances of an item from the inventory.
     *
     * @param item   The item to remove from the inventory.
     * @param amount The number of instances of the item to remove.
     */
    public void removeItems(GameObject item, int amount) {
        for (int i = 0; i < amount; i++) {
            items.remove(item);
        }
    }

    /**
     * Retrieves a list of all items in the inventory.
     *
     * @return A list of all items in the inventory.
     */
    public List<GameObject> getItems() {
        return items;
    }

    /**
     * Retrieves a specific item from the inventory based on its type.
     *
     * @param item The type of item to retrieve.
     * @return The item with the specified type, or null if not found.
     */
    public GameObject getItem(GameObjectType item) {
        for (GameObject i : items) {
            if (i.getType().equals(item)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Retrieves the maximum capacity of the inventory.
     *
     * @return The maximum capacity of the inventory.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum capacity of the inventory.
     *
     * @param capacity The new maximum capacity of the inventory.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Removes coins from the player's coin balance.
     *
     * @param amount The amount of coins to remove.
     */
    public void removeCoinFromBalance(int amount) {
        this.coinBalance -= amount;
    }

    /**
     * Sets the player's coin balance to a specific amount.
     *
     * @param coinBalance The new coin balance of the player.
     */
    public void setCoinBalance(int coinBalance) {
        this.coinBalance = coinBalance;
    }

    /**
     * Sets the items in the inventory to a specified list of items.
     *
     * @param items The new list of items for the inventory.
     */
    public void setItems(List<GameObject> items) {
        this.items = items;
    }

    /**
     * Counts the number of keys in the inventory.
     *
     * @return The number of keys in the inventory.
     */
    public int countKeys() {
        int count = 0;
        for (GameObject i : items) {
            if (i.getType().equals(GameObjectType.KEY)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if a specific quest condition is met based on the current state of the inventory.
     *
     * @return True if the quest condition is met, false otherwise.
     */
    public boolean isQuestFinished() {
        logger.log(Level.INFO, "Checking if quest is finished");
        return getCoinBalance() >= 3;
    }

    /**
     * Checks if the inventory is full (reached its maximum capacity).
     *
     * @return True if the inventory is full, false otherwise.
     */
    public boolean isFull() {
        return items.size() == capacity;
    }

    /**
     * Retrieves the size of the inventory.
     *
     * @return The size of the inventory.
     */
    public int getInventorySize() {
        return items.size();
    }
}
