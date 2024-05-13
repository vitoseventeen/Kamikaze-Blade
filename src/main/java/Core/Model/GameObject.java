package Core.Model;

import java.awt.*;

public abstract class GameObject {
    protected int x;
    protected int y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public abstract void draw(Graphics g);

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

}