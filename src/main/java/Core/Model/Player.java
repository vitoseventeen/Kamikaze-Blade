package Core.Model;

public class Player {
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private int health;
    private int score; // amount of kills
    private int speed;
    private Direction direction;
    private AnimationType animationType;
    private Inventory inventory;

    public void setHealth(int i) {
        this.health = i;
    }


    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    public enum AnimationType {
        IDLE,
        WALK,
        ATTACK,
        DEATH,
        INTERACT,
        OPEN
    }

    public Player(String name, int x, int y, int height, int width, Inventory inventory) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.inventory = inventory;
        this.name = name;
        this.health = 8;
        this.score = 0;
        this.speed = 3;
        this.direction = Direction.DOWN;
        this.animationType = AnimationType.IDLE;
    }
    public boolean checkCollisionWithEnemy(int x, int y, int width, int height) {
        return this.x < x + width &&
                this.x + this.width > x &&
                this.y < y + height &&
                this.y + this.height > y;
    }

    public int getScore() {
        return score;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void addScore() {
        this.score++;
    }

    public Object countKeys() {
        return inventory.countKeys();
    }


    public void takeDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            isDead();
            setAnimationType(AnimationType.DEATH);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }



    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getSpeed() {
        return speed;
    }



    public int getHealth() {
        return health;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }


}
