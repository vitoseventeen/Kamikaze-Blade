package Core.Model;

import java.awt.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class Chest extends GameObject {
    private boolean isOpened = false;
    private Image closedChestImage;
    private Image openedChestImage;

    public Chest(int x, int y) {
        super(x, y);
        loadImages();
    }

    private void loadImages() {
        try {
            URL closedChestURL = getClass().getResource("/lockedchest.png");
            URL openedChestURL = getClass().getResource("/openedChest.png");
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

    @Override
    public void draw(Graphics g) {
        if (isOpened) {
            g.drawImage(openedChestImage, getX(), getY(), null);
        } else {
            g.drawImage(closedChestImage, getX(), getY(), null);
        }
    }

    private int getY() {
        return y;
    }

    private int getX() {
        return x;
    }

    @Override
    public void interact(Player player) {
        // Логика взаимодействия с сундуком
    }
}


