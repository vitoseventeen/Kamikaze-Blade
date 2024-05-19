package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Represents a quest key object in the game.
 */
public class QuestKey extends GameObject {
    private transient Image keyImage;
    private static final int KEY_WIDTH = 16;
    private static final int KEY_HEIGHT = 16;

    /**
     * Constructs a quest key object at the specified position.
     *
     * @param x the x-coordinate of the quest key
     * @param y the y-coordinate of the quest key
     */
    public QuestKey(int x, int y) {
        super(x, y, GameObjectType.QUEST_KEY);
        this.setHasCollision(true);
        loadImages();
    }

    /**
     * Loads images for the quest key.
     */
    private void loadImages() {
        try {
            URL keyURL = getClass().getResource("/QuestKey.png");
            assert keyURL != null;
            keyImage = ImageIO.read(keyURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the width of the quest key.
     *
     * @return the width of the quest key
     */
    @Override
    public int getWidth() {
        return KEY_WIDTH;
    }

    /**
     * Retrieves the height of the quest key.
     *
     * @return the height of the quest key
     */
    @Override
    public int getHeight() {
        return KEY_HEIGHT;
    }

    /**
     * Draws the quest key.
     *
     * @param g the graphics object to draw on
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(keyImage, x, y, null);
    }

    /**
     * Handles player interaction with the quest key.
     *
     * @param player the player interacting with the quest key
     * @return always returns false
     */
    @Override
    public boolean interact(Player player) {
        return false;
    }
}
