package Core.Model;

public class Chest {
    private boolean isOpened = false;
    private int x;
    private int y;
    private int height;
    private int width;
    private int image;

    public Chest() {
        this.x = 0;
        this.y = 0;
        this.height = 16;
        this.width = 16;
    }
    // openedchest.png, lockedchest.png;
    public boolean isOpened() {
        return isOpened;
    }

    public void open() {
        isOpened = true;
    }

}
