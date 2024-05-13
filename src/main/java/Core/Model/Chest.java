package Core.Model;

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

    @Override
    public void draw(Graphics g) {

        // Make sure to get the correct coordinates for drawing
        if (isOpened) {
            g.drawImage(openedChestImage, x, y, null);
        } else {
            g.drawImage(closedChestImage, x, y, null);
        }
    }



    @Override
    public void interact(Player player) {
        if (!isOpened) {
            // Add some logic to interact with the player
            open();
        }
    }
}


