package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Represents a potion object in the game.
 */
public class Potion extends GameObject {

    private boolean isTaken;
    private transient Image potionImage;

    private static final int POTION_WIDTH = 9;
    private static final int POTION_HEIGHT = 11;

    /**
     * Constructs a potion object at the specified position.
     *
     * @param x the x-coordinate of the potion
     * @param y the y-coordinate of the potion
     */
    public Potion(int x, int y) {
        super(x, y, GameObjectType.POTION);
        this.isTaken = false;
        loadImages();
    }

    /**
     * Marks the potion as taken.
     */
    public void take() {
        isTaken = true;
    }

    /**
     * Draws the taken potion.
     *
     * @param g the graphics object to draw on
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     */
    public void drawTaken(Graphics g, int x, int y) {
    }

    /**
     * Retrieves the width of the potion.
     *
     * @return the width of the potion
     */
    @Override
    public int getWidth() {
        return POTION_WIDTH;
    }

    /**
     * Retrieves the height of the potion.
     *
     * @return the height of the potion
     */
    @Override
    public int getHeight() {
        return POTION_HEIGHT;
    }

    /**
     * Loads images for the potion.
     */
    private void loadImages() {
        try {
            URL potionURL = getClass().getResource("/Potion.png");
            potionImage = ImageIO.read(Objects.requireNonNull(potionURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the potion.
     *
     * @param g the graphics object to draw on
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(potionImage, x, y, null);
    }

    /**
     * Handles player interaction with the potion.
     *
     * @param player the player interacting with the potion
     * @return always returns false
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
     * Checks if the potion is taken.
     *
     * @return true if the potion is taken, false otherwise
     */
    public boolean isTaken() {
        return isTaken;
    }
}
