package Core.Model;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable {
    private int capacity;
    private List<GameObject> items;
    private int coinBalance;

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.coinBalance = 0;
        this.items = new ArrayList<>();
    }


    public int getCoinBalance() {
        return this.coinBalance;
    }

    public void addCoinToBalance(int amount) {
        this.coinBalance += amount;
    }


    public void addItem(GameObject item) {
        if (items.size() < capacity) {
            items.add(item);
        } else {
            System.out.println("Inventory is full");
        }
    }

    public void saveInventory(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    // Статический метод для загрузки инвентаря из файла
    public static Inventory loadInventory(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Inventory inventory = (Inventory) inputStream.readObject();
            System.out.println("Inventory loaded successfully.");
            return inventory;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
            return null;
        }
    }

    public void removeItem(GameObject item) {
        items.remove(item);
    }

    public void removeItems(GameObject item, int amount) {
        for (int i = 0; i < amount; i++) {
            items.remove(item);
        }
    }

    public List<GameObject> getItems() {
        return items;
    }

    public GameObject getItem(GameObjectType item) {
        for (GameObject i : items) {
            if (i.getType().equals(item)) {
                return i;
            }
        }
        return null;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void printInventory() {
        System.out.println("Inventory: ");
        for (GameObject item : items) {
            System.out.println(item.getType());
        }
        System.out.println("Coin balance: " + coinBalance);
    }

    public void removeCoinFromBalance(int amount) {
        this.coinBalance -= amount;
    }

    public void setCoinBalance(int coinBalance) {
        this.coinBalance = coinBalance;
    }

    public void setItems(List<GameObject> items) {
        this.items = items;
    }

    public int countKeys() {
        int count = 0;
        for (GameObject i : items) {
            if (i.getType().equals(GameObjectType.KEY)) {
                count++;
            }
        }
        return count;
    }



    public boolean isQuestFinished() {
        System.out.println("Checking if quest is finished");
        return getCoinBalance() >= 3;
    }

    public boolean isFull() {
        return items.size() == capacity;
    }
}
