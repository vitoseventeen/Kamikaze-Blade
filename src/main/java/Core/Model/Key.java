package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Key extends GameObject {
    private boolean isTaken;
    private Image keyImage;
    private static final int KEY_WIDTH = 16;
    private static final int KEY_HEIGHT = 16;

    public Key(int x, int y) {
        super(x, y, GameObjectType.KEY);
        this.isTaken = false;
        loadImages();
    }

    @Override
    public int getWidth() {
        return KEY_WIDTH;
    }

    @Override
    public int getHeight() {
        return KEY_HEIGHT;
    }

    private void loadImages() {
        try {
            URL keyURL = getClass().getResource("/Key.png");
            keyImage = ImageIO.read(keyURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void take() {
        isTaken = true;
    }


    public void drawTaken(Graphics g, int x, int y) {
        //TODO remove from screen and add to inventory
    }


    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(keyImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        if (!isTaken) {
            take();
            player.getInventory().addItem(this);
        }
        return false;
    }
}
