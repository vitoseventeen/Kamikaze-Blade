package Core.Model;


import static Core.Util.Constants.ATTACK_COOLDOWN;

/**
 * Represents an enemy in the game.
 * Inherits from the Player class.
 */
public class Enemy extends Player {
    private int dx;
    private int dy;
    private final int speed;
    private int health;
    private long lastAttackTime;

    /**
     * Constructs an enemy with the specified attributes.
     *
     * @param name   The name of the enemy.
     * @param x      The x-coordinate of the enemy.
     * @param y      The y-coordinate of the enemy.
     * @param height The height of the enemy.
     * @param width  The width of the enemy.
     */
    public Enemy(String name, int x, int y, int height, int width) {
        super(name, x, y, height, width, null);
        this.speed = 1;
        this.health = 3;
    }

    /**
     * Checks if the enemy can attack based on the attack cooldown.
     *
     * @return true if the enemy can attack, false otherwise.
     */
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= ATTACK_COOLDOWN;
    }

    /**
     * Checks if the enemy is dead.
     *
     * @return true if the enemy is dead, false otherwise.
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Reduces the enemy's health by the specified damage amount.
     *
     * @param damage The amount of damage to reduce the health by.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            isDead();
        }
    }

    /**
     * Gets the speed of the enemy.
     *
     * @return The speed of the enemy.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the health of the enemy.
     *
     * @return The health of the enemy.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets the horizontal velocity of the enemy.
     *
     * @return The horizontal velocity of the enemy.
     */
    public int getDx() {
        return dx;
    }

    /**
     * Sets the horizontal velocity of the enemy.
     *
     * @param dx The horizontal velocity to set for the enemy.
     */
    public void setDx(int dx) {
        this.dx = dx;
    }

    /**
     * Gets the vertical velocity of the enemy.
     *
     * @return The vertical velocity of the enemy.
     */
    public int getDy() {
        return dy;
    }

    /**
     * Sets the vertical velocity of the enemy.
     *
     * @param dy The vertical velocity to set for the enemy.
     */
    public void setDy(int dy) {
        this.dy = dy;
    }

    /**
     * Checks collision between the enemy and a specified rectangle area.
     *
     * @param x      The x-coordinate of the rectangle area.
     * @param y      The y-coordinate of the rectangle area.
     * @param width  The width of the rectangle area.
     * @param height The height of the rectangle area.
     * @return true if collision occurs, false otherwise.
     */
    public boolean checkCollision(int x, int y, int width, int height) {
        return this.getX() < x + width &&
                this.getX() + this.getWidth() > x &&
                this.getY() < y + height &&
                this.getY() + this.getHeight() > y;
    }

    /**
     * Sets the last attack time of the enemy.
     *
     * @param l The last attack time to set.
     */
    public void setLastAttackTime(long l) {
        this.lastAttackTime = l;
    }


}
