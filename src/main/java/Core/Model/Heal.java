package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Heal extends GameObject{

    private boolean isTaken;
    private Image healImage;

    private static final int HEAL_WIDTH = 10;
    private static final int HEAL_HEIGHT = 10;

    public Heal(int x, int y) {
        super(x, y, GameObjectType.HEAL);
        this.isTaken = false;
        loadImages();
    }

    @Override
    public int getWidth() {
        return HEAL_WIDTH;
    }

    @Override
    public int getHeight() {
        return HEAL_HEIGHT;
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

    private void loadImages() {
        try {
            URL healURL = getClass().getResource("/Heal.png");
            healImage = ImageIO.read(healURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(healImage, x, y, null);
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
