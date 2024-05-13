package Core.Model;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class Chest extends GameObject {
    private boolean isOpened = false;
    private Image closedChestImage;
    private Image openedChestImage;
    private static final int CHEST_WIDTH = 16;
    private static final int CHEST_HEIGHT = 16;

    public Chest(int x, int y) {
        super(x, y, ObjectType.CHEST);
        loadImages();
    }

    @Override
    public int getWidth() {
        return CHEST_WIDTH;
    }

    @Override
    public int getHeight() {
        return CHEST_HEIGHT;
    }

    private void loadImages() {
        try {
            URL closedChestURL = getClass().getResource("/chestClosed.png");
            URL openedChestURL = getClass().getResource("/chestOpened.png");
            closedChestImage = ImageIO.read(closedChestURL);
            openedChestImage = ImageIO.read(openedChestURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void open() {
        isOpened = true;
    }


    public void drawOpened(Graphics g, int x, int y) {
        g.drawImage(openedChestImage, x, y, null);
    }


    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(closedChestImage, x, y, null);
    }

    @Override
    public void interact(Player player) {
        if (!isOpened) {
            open();
        }
    }
}


