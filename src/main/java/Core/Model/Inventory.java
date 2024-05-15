package Core.Model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
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
}
