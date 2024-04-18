package Core.Model;

public class Player extends Entity {

    /* Trida hrac dedi tridu Entity a ma svoje vlastnosti, metody*/
    private String name;
    private int health;
    private int score;
    private int speed;

    public Player(String name,int x, int y) {
        super(x,y);
        this.name = name;
        this.health = 100;
        this.score = 0;
        this.speed = 11;
    }



    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void moveLeft() {
        this.x -= speed;
    }

    public void moveRight() {
        this.x += speed;
    }

    public void moveUp() {
        this.y -= speed;
    }

    public void moveDown() {
        this.y += speed;
    }
    public void jump() {

    }

    public void attack() {
    }

    public void die() {

    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void collectCoin(int value) {
        this.score += value;
    }
}
