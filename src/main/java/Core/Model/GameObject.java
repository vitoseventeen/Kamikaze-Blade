package Core.Model;

import java.awt.*;

import static Core.Util.Constants.PLAYER_HEIGHT;
import static Core.Util.Constants.PLAYER_WIDTH;


public abstract class GameObject {
    protected int x;
    protected int y;
    private boolean isInteracted = false;
    private boolean hasCollision = false;
    private GameObjectType type;

    public GameObject(int x, int y, GameObjectType type) {
        this.x = x;
        this.y = y;
        this.isInteracted = false;
        this.hasCollision = false;
        this.type = type;
    }

    public boolean isInteracted() {
        return isInteracted;
    }

    public boolean hasCollision() {
        return hasCollision;
    }

    public GameObjectType getType() {
        return type;
    }

    public abstract void draw(Graphics g, int x, int y);

    public abstract boolean interact(Player player);

    public boolean checkCollision(int x, int y, int width, int height) {
        return this.x < x + width &&
                this.x + PLAYER_WIDTH > x &&
                this.y < y + height &&
                this.y + PLAYER_HEIGHT > y;
    }

    public void setInteracted(boolean interacted) {
        isInteracted = interacted;
    }

    public void setHasCollision(boolean collision) {
        hasCollision = collision;
    }

    public String getX() {
        return String.valueOf(x);
    }

    public String getY() {
        return String.valueOf(y);
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }
}
