package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Represents a key item in the game world.
 * Keys are used to unlock doors and access restricted areas.
 */
public class Key extends GameObject {
    private boolean isTaken; // Indicates whether the key has been taken by the player
    private transient Image keyImage; // The image representing the key
    private static final int KEY_WIDTH = 16; // The width of the key image
    private static final int KEY_HEIGHT = 16; // The height of the key image

    /**
     * Constructs a new Key object at the specified position.
     *
     * @param x The x-coordinate of the key's position.
     * @param y The y-coordinate of the key's position.
     */
    public Key(int x, int y) {
        super(x, y, GameObjectType.KEY);
        this.isTaken = false;
        this.setHasCollision(true);
        loadImages();
    }

    /**
     * Retrieves the width of the key.
     *
     * @return The width of the key.
     */
    @Override
    public int getWidth() {
        return KEY_WIDTH;
    }

    /**
     * Retrieves the height of the key.
     *
     * @return The height of the key.
     */
    @Override
    public int getHeight() {
        return KEY_HEIGHT;
    }

    /**
     * Loads the image of the key from the resource directory.
     */
    private void loadImages() {
        try {
            URL keyURL = getClass().getResource("/Key.png");
            keyImage = ImageIO.read(Objects.requireNonNull(keyURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the key has been taken by the player.
     *
     * @return True if the key has been taken, false otherwise.
     */
    public boolean isTaken() {
        return isTaken;
    }

    /**
     * Marks the key as taken by the player.
     */
    public void take() {
        isTaken = true;
    }

    /**
     * Draws the key image on the screen.
     *
     * @param g The graphics context to draw the key image.
     * @param x The x-coordinate at which to draw the key image.
     * @param y The y-coordinate at which to draw the key image.
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(keyImage, x, y, null);
    }

    /**
     * Handles interaction between the player and the key.
     * If the key has not been taken and the player's inventory is not full, the key is taken by the player.
     *
     * @param player The player interacting with the key.
     * @return Always returns false as key interaction does not affect game state.
     */
    @Override
    public boolean interact(Player player) {
        if (!isTaken && !player.getInventory().isFull()) {
            take();
            player.getInventory().addItem(this);
        }
        return false;
    }

    /**
     * Draws the taken state of the key.
     * This method is intended to be overridden by subclasses to draw any additional visual effects
     * when the coin is collected.
     *
     * @param g         The graphics object to draw on.
     * @param objectX   The x-coordinate of the object.
     * @param objectY   The y-coordinate of the object.
     */
    public void drawTaken(Graphics g, int objectX, int objectY) {
    }
}
