package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Represents a coin object in the game.
 */
public class Coin extends GameObject {
    private final int value;
    private boolean isCollected;
    private transient Image coinImage;
    private static final int COIN_WIDTH = 10;
    private static final int COIN_HEIGHT = 10;

    /**
     * Constructs a coin object at the specified position.
     *
     * @param x The x-coordinate of the coin.
     * @param y The y-coordinate of the coin.
     */
    public Coin(int x, int y) {
        super(x, y, GameObjectType.COIN);
        this.value = 1;
        this.setHasCollision(true);
        loadImages();
    }

    /**
     * Loads the image for the coin.
     */
    private void loadImages() {
        try {
            URL coinURL = getClass().getResource("/coin.gif");
            coinImage = ImageIO.read(Objects.requireNonNull(coinURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the width of the coin.
     *
     * @return The width of the coin.
     */
    @Override
    public int getWidth() {
        return COIN_WIDTH;
    }

    /**
     * Gets the height of the coin.
     *
     * @return The height of the coin.
     */
    @Override
    public int getHeight() {
        return COIN_HEIGHT;
    }

    /**
     * Checks if the coin is collected.
     *
     * @return true if the coin is collected, false otherwise.
     */
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Gets the value of the coin.
     *
     * @return The value of the coin.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the coin as collected.
     */
    public void collect() {
        isCollected = true;
    }

    /**
     * Draws the coin.
     *
     * @param g      The graphics object to draw on.
     * @param x      The x-coordinate to draw the coin.
     * @param y      The y-coordinate to draw the coin.
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(coinImage, x, y, null);
    }

    /**
     * Interacts with the player.
     * If the coin is not collected, it adds the coin's value to the player's balance and marks the coin as collected.
     *
     * @param player The player object to interact with.
     * @return Always returns false.
     */
    @Override
    public boolean interact(Player player) {
        if (!isCollected) {
            player.getInventory().addCoinToBalance(value);
            collect();
        }
        return false;
    }

    /**
     * Draws the collected state of the coin.
     * This method is intended to be overridden by subclasses to draw any additional visual effects
     * when the coin is collected.
     *
     * @param g         The graphics object to draw on.
     * @param objectX   The x-coordinate of the object.
     * @param objectY   The y-coordinate of the object.
     */
    public void drawCollected(Graphics g, int objectX, int objectY) {
    }

}
