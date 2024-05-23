package Model;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Represents a chest object in the game.
 */
public class Chest extends GameObject {
    private boolean isOpened = false;
    private Image closedChestImage;
    private Image openedChestImage;
    private static final int CHEST_WIDTH = 16;
    private static final int CHEST_HEIGHT = 16;

    /**
     * Constructs a chest object at the specified position.
     * @param x The x-coordinate of the chest.
     * @param y The y-coordinate of the chest.
     */
    public Chest(int x, int y) {
        super(x, y, GameObjectType.CHEST);
        loadImages();
    }


    /**
     * Loads images for the closed and opened chest.
     */
    private void loadImages() {
        try {
            URL closedChestURL = getClass().getResource("/chestClosed.png");
            URL openedChestURL = getClass().getResource("/chestOpened.png");
            closedChestImage = ImageIO.read(Objects.requireNonNull(closedChestURL));
            openedChestImage = ImageIO.read(Objects.requireNonNull(openedChestURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the chest is opened.
     * @return true if the chest is opened, false otherwise.
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * Opens the chest.
     */
    public void open() {
        isOpened = true;
    }

    /**
     * Draws the opened chest.
     * @param g The graphics object to draw on.
     * @param x The x-coordinate to draw the chest.
     * @param y The y-coordinate to draw the chest.
     */
    public void drawOpened(Graphics g, int x, int y) {
        g.drawImage(openedChestImage, x, y, null);
    }

    /**
     * Draws the closed chest.
     * @param g The graphics object to draw on.
     * @param x The x-coordinate to draw the chest.
     * @param y The y-coordinate to draw the chest.
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(closedChestImage, x, y, null);
    }

    /**
     * Interacts with the player.
     * If the chest is not opened, it opens the chest.
     * @param player The player object to interact with.
     * @return Always returns false.
     */
    @Override
    public boolean interact(Player player) {
        if (!isOpened) {
            open();
        }
        return false;
    }
}
