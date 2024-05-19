package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Represents a door that serves as a passage between levels in the game.
 */
public class LevelDoor extends GameObject {

    private boolean isOpened = false;
    private Image closedLevelDoorImage;
    private Image openedLevelDoorImage;
    private static final int LEVEL_DOOR_WIDTH = 64;
    private static final int LEVEL_DOOR_HEIGHT = 64;

    /**
     * Constructs a level door object at the specified position.
     *
     * @param x the x-coordinate of the level door
     * @param y the y-coordinate of the level door
     */
    public LevelDoor(int x, int y) {
        super(x, y, GameObjectType.LEVELDOOR);
        loadImages();
        this.setHasCollision(true);
    }

    /**
     * Retrieves the width of the level door.
     *
     * @return the width of the level door
     */
    @Override
    public int getWidth() {
        return LEVEL_DOOR_WIDTH;
    }

    /**
     * Retrieves the height of the level door.
     *
     * @return the height of the level door
     */
    @Override
    public int getHeight() {
        return LEVEL_DOOR_HEIGHT;
    }

    /**
     * Loads images for the closed and opened level doors.
     */
    private void loadImages() {
        try {
            URL closedLevelDoorURL = getClass().getResource("/levelDoorClosed.png");
            URL openedLevelDoorURL = getClass().getResource("/levelDoorOpened.png");
            closedLevelDoorImage = ImageIO.read(Objects.requireNonNull(closedLevelDoorURL));
            openedLevelDoorImage = ImageIO.read(Objects.requireNonNull(openedLevelDoorURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the level door is opened.
     *
     * @return true if the level door is opened, false otherwise
     */
    public boolean isOpened() {
        this.setHasCollision(false);
        return isOpened;
    }

    /**
     * Opens the level door.
     */
    public void open() {
        isOpened = true;
    }

    /**
     * Draws the opened level door image.
     *
     * @param g the graphics object to draw on
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     */
    public void drawOpened(Graphics g, int x, int y) {
        g.drawImage(openedLevelDoorImage, x, y, null);
    }

    /**
     * Draws the closed level door image.
     *
     * @param g the graphics object to draw on
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(closedLevelDoorImage, x, y, null);
    }

    /**
     * Handles player interaction with the level door.
     *
     * @param player the player interacting with the door
     * @return always returns false
     */
    @Override
    public boolean interact(Player player) {
        QuestKey questKey = (QuestKey) player.getInventory().getItem(GameObjectType.QUEST_KEY);
        if (!isOpened) {
            if (player.getInventory().getItem(GameObjectType.QUEST_KEY) != null && !isOpened) {
                player.getInventory().removeItem(questKey);
                open();
            }
        }
        return false;
    }
}
