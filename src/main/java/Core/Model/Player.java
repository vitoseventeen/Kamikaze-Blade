package Core.Model;

public class Player {
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private int health;
    private int score;
    private int speed;
    private Direction direction;
    private AnimationType animationType;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    public enum AnimationType {
        IDLE,
        WALK,
        ATTACK
    }

    public Player(String name, int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.name = name;
        this.health = 100;
        this.score = 0;
        this.speed = 2;
        this.direction = Direction.DOWN;
        this.animationType = AnimationType.IDLE;
    }
    public boolean checkCollisionWithEnemy(int x, int y, int width, int height) {
        return this.x < x + width &&
                this.x + this.width > x &&
                this.y < y + height &&
                this.y + this.height > y;
    }



    public int getSpeed() {
        return speed;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            die();
        }
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

    private void die() {
    }

    public boolean checkCollisionWithEnemy(Enemy enemy) {
        if (this.x < enemy.getX() + enemy.getWidth() &&
                this.x + this.getWidth() > enemy.getX() &&
                this.y < enemy.getY() + enemy.getHeight() &&
                this.y + this.getHeight() > enemy.getY()) {
            return true;
        }
        return false;
    }

    public void collectCoin(int value) {
        this.score += value;
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
