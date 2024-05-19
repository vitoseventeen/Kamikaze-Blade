package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Represents a heal item in the game world.
 * It can be picked up by the player to restore health.
 */
public class Heal extends GameObject {

    private boolean isTaken; // Flag indicating whether the heal item has been taken
    private transient Image healImage; // Image representing the heal item

    private static final int HEAL_WIDTH = 10; // Width of the heal item
    private static final int HEAL_HEIGHT = 10; // Height of the heal item

    /**
     * Constructs a heal item at the specified position.
     *
     * @param x The x-coordinate of the heal item.
     * @param y The y-coordinate of the heal item.
     */
    public Heal(int x, int y) {
        super(x, y, GameObjectType.HEAL);
        this.isTaken = false;
        loadImages();
    }

    @Override
    public int getWidth() {
        return HEAL_WIDTH;
    }

    @Override
    public int getHeight() {
        return HEAL_HEIGHT;
    }

    /**
     * Checks if the heal item has been taken.
     *
     * @return true if the heal item has been taken, false otherwise.
     */
    public boolean isTaken() {
        return isTaken;
    }

    /**
     * Sets the heal item as taken.
     */
    public void take() {
        isTaken = true;
    }

    /**
     * Draws the heal item when it's taken by the player.
     * This method is called when the heal item is in the player's inventory.
     *
     * @param g The graphics object to draw on.
     * @param x The x-coordinate to draw the heal item.
     * @param y The y-coordinate to draw the heal item.
     */
    public void drawTaken(Graphics g, int x, int y) {
    }

    /**
     * Loads the image of the heal item.
     */
    private void loadImages() {
        try {
            URL healURL = getClass().getResource("/Heal.png");
            healImage = ImageIO.read(Objects.requireNonNull(healURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(healImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        // If the heal item hasn't been taken and the player's inventory is not full,
        // the heal item is taken by the player and added to the inventory
        if (!isTaken && !player.getInventory().isFull()) {
            take();
            player.getInventory().addItem(this);
        }
        return false;
    }
}
