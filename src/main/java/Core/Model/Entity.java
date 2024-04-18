package Core.Model;

public abstract class Entity {
    /* abstraktní třída zděděná hráčem a nepřítelem */
    protected int x,y;
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
