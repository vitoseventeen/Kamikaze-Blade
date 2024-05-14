package Core.Model;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

import static Core.Util.Constants.TILE_SIZE;

public class Door extends GameObject {
    private boolean isOpened = false;
    private Image closedDoorImage;
    private Image openedDoorImage;
    private static final int DOOR_WIDTH = 64;
    private static final int DOOR_HEIGHT = 64;

    public Door(int x, int y) {
        super(x, y, ObjectType.DOOR);
        loadImages();

    }

    public int getTileX() {
        return x / TILE_SIZE;
    }

    public int getTileY() {
         return y / TILE_SIZE;
    }




    @Override
    public int getWidth() {
        System.out.println("x " + getTileX());
        return DOOR_WIDTH;

    }

    @Override
    public int getHeight() {
        System.out.println("y " + getTileY());
        return DOOR_HEIGHT;
    }

    private void loadImages() {
        try {
            URL closedDoorURL = getClass().getResource("/doorClosed.png");
            URL openedDoorURL = getClass().getResource("/doorOpened.png");
            closedDoorImage = ImageIO.read(closedDoorURL);
            openedDoorImage = ImageIO.read(openedDoorURL);
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
        g.drawImage(openedDoorImage, x, y, null);
    }


    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(closedDoorImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        if (!isOpened ) {
            if (player.getInventory().getItem(ObjectType.KEY)) {
                player.getInventory().removeItem(ObjectType.KEY);
                open();
            }
        }
        return false;

    }
}


