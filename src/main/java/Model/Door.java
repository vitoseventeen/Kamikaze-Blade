package Model;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;


/**
 * Represents a door object in the game.
 */
public class Door extends GameObject {
    private boolean isOpened = false;
    private transient Image closedDoorImage;
    private transient Image openedDoorImage;

    /**
     * Constructs a door object at the specified position.
     *
     * @param x The x-coordinate of the door.
     * @param y The y-coordinate of the door.
     */
    public Door(int x, int y) {
        super(x, y, GameObjectType.DOOR);
        loadImages();
        this.setHasCollision(true);
    }


    /**
     * Loads images for the closed and opened door.
     */
    private void loadImages() {
        try {
            URL closedDoorURL = getClass().getResource("/doorClosed.png");
            URL openedDoorURL = getClass().getResource("/doorOpened.png");
            closedDoorImage = ImageIO.read(Objects.requireNonNull(closedDoorURL));
            openedDoorImage = ImageIO.read(Objects.requireNonNull(openedDoorURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the door is opened.
     *
     * @return true if the door is opened, false otherwise.
     */
    public boolean isOpened() {
        this.setHasCollision(false);
        return isOpened;
    }

    /**
     * Opens the door.
     */
    public void open() {
        isOpened = true;
    }

    /**
     * Draws the opened door.
     *
     * @param g The graphics object to draw on.
     * @param x The x-coordinate to draw the door.
     * @param y The y-coordinate to draw the door.
     */
    public void drawOpened(Graphics g, int x, int y) {
        g.drawImage(openedDoorImage, x, y, null);
    }

    /**
     * Draws the closed door.
     *
     * @param g The graphics object to draw on.
     * @param x The x-coordinate to draw the door.
     * @param y The y-coordinate to draw the door.
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(closedDoorImage, x, y, null);
    }

    /**
     * Interacts with the player.
     * If the door is closed and the player has a key, the door opens and the key is consumed.
     *
     * @param player The player object to interact with.
     * @return Always returns false.
     */
    @Override
    public boolean interact(Player player) {
        Key key = (Key) player.getInventory().getItem(GameObjectType.KEY);
        if (!isOpened) {
            if (player.getInventory().getItem(GameObjectType.KEY) != null && !isOpened && key.isTaken()) {
                player.getInventory().removeItem(key);
                open();
            }
        }
        return false;
    }
}
