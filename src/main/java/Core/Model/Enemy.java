package Core.Model;

import Core.Controller.Controller;

import static Core.Util.Constants.ATTACK_COOLDOWN;

public class Enemy extends Player {
    private int dx;
    private int dy;
    private int speed;
    private int health;
    private long lastAttackTime;


    public Enemy(String name, int x, int y, int height, int width) {
        super(name, x, y, height, width, null);
        this.speed = 1;
        this.health = 3;
    }

    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= ATTACK_COOLDOWN;
    }


    public boolean isDead() {
        return health <= 0;

    }


    public void takeDamage(int damage) {
        this.health -= damage;
        if (health <= 0) {
            isDead();
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }



    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
    public boolean checkCollision(int x, int y, int width, int height) {
        return this.getX() < x + width &&
                this.getX() + this.getWidth() > x &&
                this.getY() < y + height &&
                this.getY() + this.getHeight() > y;
    }

    public void setLastAttackTime(long l) {
        this.lastAttackTime = l;
    }
}
