package Core.Model;

public class Player extends Character {
    private String name;
    private int health;
    private int score;
    private int speed;

    public Player(String name, int x, int y, int height, int width) {
        super(x, y, height, width);
        this.name = name;
        this.health = 100;
        this.score = 0;
        this.speed = 30;
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
}
