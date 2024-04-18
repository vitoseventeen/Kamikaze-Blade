package Core.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Character {

    /* Trida hrac dedi tridu Entity a ma svoje vlastnosti, metody*/
    private String name;
    private int health;
    private int score;
    private int speed;

    public Player(String name,int x, int y, int height, int width) {
        super(x,y,height,width);
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

    public Rectangle getPlayerBounds() {
        return new Rectangle(x, y, width, height);
    }

//    public boolean checkCollisionWithLevel() {
//
//    }

    public boolean checkCollisionWithEnemy(Enemy enemy) { // TODO: create enemy
        if (this.x < enemy.getX() + enemy.getWidth() &&
                this.x + this.getWidth() > enemy.getX() &&
                this.y < enemy.getY() + enemy.getHeight() &&
                this.y + this.getHeight() > enemy.getY()) {
            // collision
            return true;
        }
        return false;
    }


    public void collectCoin(int value) {
        this.score += value;
    }
}
