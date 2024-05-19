package Model;

/**
 * Represents the player character in the game.
 */
public class Player {
    private int x;
    private int y;
    private int width;
    private int height;
    private final String name;
    private int health;
    private int score; // amount of kills
    private final int speed;
    private Direction direction;
    private AnimationType animationType;
    private Inventory inventory;

    /**
     * Retrieves the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the score of the player.
     *
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Enumeration representing the direction of the player character.
     */
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    /**
     * Enumeration representing the animation type of the player character.
     */
    public enum AnimationType {
        IDLE,
        WALK,
        ATTACK,
        DEATH,
        INTERACT,
        OPEN
    }

    /**
     * Constructs a player character with the specified attributes.
     *
     * @param name      the name of the player
     * @param x         the initial x-coordinate of the player
     * @param y         the initial y-coordinate of the player
     * @param height    the height of the player
     * @param width     the width of the player
     * @param inventory the inventory of the player
     */
    public Player(String name, int x, int y, int height, int width, Inventory inventory) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.inventory = inventory;
        this.name = name;
        this.health = 3;
        this.score = 0;
        this.speed = 3;
        this.direction = Direction.DOWN;
        this.animationType = AnimationType.IDLE;
    }

    /**
     * Sets the health of the player.
     *
     * @param health the health to set
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Checks for collision with an enemy.
     *
     * @param x      the x-coordinate of the enemy
     * @param y      the y-coordinate of the enemy
     * @param width  the width of the enemy
     * @param height the height of the enemy
     * @return true if collision occurs, false otherwise
     */
    public boolean checkCollisionWithEnemy(int x, int y, int width, int height) {
        return this.x < x + width &&
                this.x + this.width > x &&
                this.y < y + height &&
                this.y + this.height > y;
    }

    /**
     * Retrieves the score of the player.
     *
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks if the player is dead.
     *
     * @return true if the player is dead, false otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Increments the score of the player.
     */
    public void addScore() {
        this.score++;
    }

    /**
     * Counts the number of keys in the player's inventory.
     *
     * @return the number of keys
     */
    public Object countKeys() {
        return inventory.countKeys();
    }

    /**
     * Inflicts damage to the player.
     *
     * @param damage the amount of damage to inflict
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            isDead();
            setAnimationType(AnimationType.DEATH);
        }
    }

    /**
     * Retrieves the inventory of the player.
     *
     * @return the inventory of the player
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets the inventory of the player.
     *
     * @param inventory the inventory to set
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Retrieves the speed of the player.
     *
     * @return the speed of the player
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Retrieves the health of the player.
     *
     * @return the health of the player
     */
    public int getHealth() {
        return health;
    }

    /**
     * Retrieves the width of the player.
     *
     * @return the width of the player
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the player.
     *
     * @return the height of the player
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the x-coordinate of the player.
     *
     * @return the x-coordinate of the player
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the player.
     *
     * @param x the x-coordinate to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Retrieves the y-coordinate of the player.
     *
     * @return the y-coordinate of the player
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the player.
     *
     * @param y the y-coordinate to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the height of the player.
     *
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the width of the player.
     *
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Retrieves the direction of the player.
     *
     * @return the direction of the player
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the player.
     *
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Retrieves the animation type of the player.
     *
     * @return the animation type of the player
     */
    public AnimationType getAnimationType() {
        return animationType;
    }

    /**
     * Sets the animation type of the player.
     *
     * @param animationType the animation type to set
     */
    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }
}
