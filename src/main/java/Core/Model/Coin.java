package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Coin extends GameObject {
    private final int value;
    private boolean isCollected;
    private Image coinImage;
    private static final int COIN_WIDTH = 16;
    private static final int COIN_HEIGHT = 16;

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
    public void interact(Player player) {
        if (!isCollected) {
            player.getInventory().addCoinToBalance(value);
            collect();
        }
    }

    public void drawCollected(Graphics g, int objectX, int objectY)
    {
    }
}
