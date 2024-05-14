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


    public boolean containsItem(ObjectType item) {
        for (GameObject i : items) {
            return i.equals(item);
        }
        return false;
    }


    public boolean addItem(GameObject item) {
        if (items.size() < capacity) {
            items.add(item);
            this.capacity -= 1;
            return true;
        }
        else {
            System.out.println("Inventory is full");
            return false;
        }
    }

    public boolean removeItem(ObjectType item) {
        return items.remove(item);
    }



    public List<GameObject> getItems() {
        return items;
    }


    public boolean getItem(ObjectType item) {
        for (GameObject i : items) {
            if (i.getType().equals(item)) {
                return true;
            }
        }
        return false;
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


}
