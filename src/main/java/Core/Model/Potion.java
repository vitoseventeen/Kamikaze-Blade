package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Potion extends GameObject{

    private boolean isTaken;
    private transient Image potionImage;

    private static final int POTION_WIDTH = 9;
    private static final int POTION_HEIGHT = 11;

    public Potion(int x, int y) {
        super(x, y, GameObjectType.POTION);
        this.isTaken = false;
        loadImages();
    }


    public void take() {
        isTaken = true;
    }


    public void drawTaken(Graphics g, int x, int y) {
        //TODO remove from screen and add to inventory
    }

    @Override
    public int getWidth() {
        return POTION_WIDTH;
    }

    @Override
    public int getHeight() {
        return POTION_HEIGHT;
    }

    private void loadImages() {
        try {
            URL potionURL = getClass().getResource("/Potion.png");
            potionImage = ImageIO.read(potionURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(potionImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        if (!isTaken && !player.getInventory().isFull()) {
            take();
            player.getInventory().addItem(this);
        }
        return false;
    }

    public boolean isTaken() {
        return isTaken;
    }

}
