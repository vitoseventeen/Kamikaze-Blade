package Core.Model;

public class Enemy extends Player {
    private int dx;
    private int dy;

    public Enemy(String name, int x, int y, int height, int width) {
        super(name, x, y, height, width);
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

}
