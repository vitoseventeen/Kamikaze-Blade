package Model;

import java.awt.*;
import java.io.Serializable;

import static Util.Constants.PLAYER_HEIGHT;
import static Util.Constants.PLAYER_WIDTH;

/**
 * Represents a game object in the game world.
 * This is an abstract class to be extended by specific types of game objects.
 */
public abstract class GameObject implements Serializable {
    protected int x;
    protected int y;
    private boolean isInteracted = false;
    private boolean hasCollision = false;
    private final GameObjectType type;

    /**
     * Constructs a game object at the specified position with the given type.
     *
     * @param x    The x-coordinate of the game object.
     * @param y    The y-coordinate of the game object.
     * @param type The type of the game object.
     */
    public GameObject(int x, int y, GameObjectType type) {
        this.x = x;
        this.y = y;
        this.isInteracted = false;
        this.hasCollision = true;
        this.type = type;
    }

    /**
     * Checks if the game object has been interacted with.
     *
     * @return true if the game object has been interacted with, false otherwise.
     */
    public boolean isInteracted() {
        return isInteracted;
    }

    /**
     * Checks if the game object has collision.
     *
     * @return true if the game object has collision, false otherwise.
     */
    public boolean hasCollision() {
        return hasCollision;
    }

    /**
     * Gets the type of the game object.
     *
     * @return The type of the game object.
     */
    public GameObjectType getType() {
        return type;
    }

    /**
     * Draws the game object.
     *
     * @param g The graphics object to draw on.
     * @param x The x-coordinate to draw the game object.
     * @param y The y-coordinate to draw the game object.
     */
    public abstract void draw(Graphics g, int x, int y);

    /**
     * Interacts with the player.
     *
     * @param player The player object to interact with.
     * @return true if interaction was successful, false otherwise.
     */
    public abstract boolean interact(Player player);

    /**
     * Checks collision between the game object and a specified rectangle area.
     *
     * @param x      The x-coordinate of the rectangle area.
     * @param y      The y-coordinate of the rectangle area.
     * @param width  The width of the rectangle area.
     * @param height The height of the rectangle area.
     * @return true if collision occurs, false otherwise.
     */
    public boolean checkCollision(int x, int y, int width, int height) {
        return this.x < x + width &&
                this.x + PLAYER_WIDTH > x &&
                this.y < y + height &&
                this.y + PLAYER_HEIGHT > y;
    }

    /**
     * Sets whether the game object has been interacted with.
     *
     * @param interacted true if the game object has been interacted with, false otherwise.
     */
    public void setInteracted(boolean interacted) {
        isInteracted = interacted;
    }

    /**
     * Sets whether the game object has collision.
     *
     * @param collision true if the game object has collision, false otherwise.
     */
    public void setHasCollision(boolean collision) {
        hasCollision = collision;
    }

    /**
     * Gets the x-coordinate of the game object.
     *
     * @return The x-coordinate of the game object.
     */
    public String getX() {
        return String.valueOf(x);
    }

    /**
     * Gets the y-coordinate of the game object.
     *
     * @return The y-coordinate of the game object.
     */
    public String getY() {
        return String.valueOf(y);
    }

    /**
     * Gets the width of the game object.
     * This method should be overridden by subclasses if applicable.
     *
     * @return The width of the game object.
     */
    public int getWidth() {
        return 0;
    }

    /**
     * Gets the height of the game object.
     * This method should be overridden by subclasses if applicable.
     *
     * @return The height of the game object.
     */
    public int getHeight() {
        return 0;
    }
}
