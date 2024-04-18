package Core.Model;

public abstract class Character {
    /* abstraktní třída zděděná hráčem a nepřítelem */
    protected int x,y,height,width;
    public Character(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = 32;
        this.width = 32;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
