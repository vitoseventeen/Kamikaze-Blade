package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Coin extends GameObject {
    private final int value;
    private boolean isCollected;
    private Image coinImage;
    private static final int COIN_WIDTH = 10;
    private static final int COIN_HEIGHT = 10;

    public Coin(int x, int y) {
        super(x, y, ObjectType.COIN);
        this.value = 1;
        loadImages();
    }

    private void loadImages() {
        try {
            URL coinURL = getClass().getResource("/coin.gif");
            coinImage = ImageIO.read(coinURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return COIN_WIDTH;
    }

    @Override
    public int getHeight() {
        return COIN_HEIGHT;
    }


    public boolean isCollected() {
        return isCollected;
    }

    public int getValue() {
        return value;
    }

    public void collect() {
        isCollected = true;

    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(coinImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        if (!isCollected) {
            player.getInventory().addCoinToBalance(value);
            player.getInventory().printInventory();
            collect();
        }
        return false;
    }

    public void drawCollected(Graphics g, int objectX, int objectY)
    {
    }
}
