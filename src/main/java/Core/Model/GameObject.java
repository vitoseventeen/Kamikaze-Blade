package Core.Model;

import Core.Controller.Controller;
import Core.View.GamePanel;

import java.awt.*;

import static Core.Util.Constants.PLAYER_HEIGHT;
import static Core.Util.Constants.PLAYER_WIDTH;

public abstract class GameObject {
    protected int x;
    protected int y;
    private boolean isInteracted = false;
    private boolean hasCollision = false;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        this.isInteracted = false;
        this.hasCollision = false;
    }


    public boolean isInteracted() {
        return isInteracted;
    }

    public boolean hasCollision() {
        return hasCollision;
    }

    public abstract void draw(Graphics g, int x, int y);

    public abstract void interact(Player player);

    public String getString(String type) {
        return type;
    }


    public String getX() {
        return String.valueOf(x);
    }

    public String getY() {
        return String.valueOf(y);
    }


    public boolean checkCollision(int x, int y, int width, int height) {
        return this.x < x + width &&
                this.x + PLAYER_WIDTH > x &&
                this.y < y + height &&
                this.y + PLAYER_HEIGHT > y;
    }
}