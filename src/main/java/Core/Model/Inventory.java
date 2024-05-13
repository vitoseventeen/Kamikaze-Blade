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

    public String getItemsString() {
        StringBuilder itemsString = new StringBuilder();
        for (GameObject item : items) {
            itemsString.append(item.getType()).append(" ");
        }
        return itemsString.toString();
    }

    @Override
    public String toString() {
        return "Inventory " +
                "capacity is: " + capacity +
                ", my items are: " + items + " " ;
    }

    public boolean addItem(GameObject item) {
        if (items.size() < capacity) {
            items.add(item);
            this.capacity -= 1;
            return true;
        }
        System.out.println("Sorry, your inventory is full :(");
        return false;
    }

    public boolean removeItem(GameObject item) {
        return items.remove(item);
    }

    public List<GameObject> getItems() {
        return items;
    }

    public boolean contains(GameObject item) {
        return items.contains(item);
    }

    public int getCapacity() {
        return capacity;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
