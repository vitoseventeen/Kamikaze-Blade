package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class QuestKey extends GameObject{
    private boolean isTaken;
    private Image keyImage;
    private static final int KEY_WIDTH = 16;
    private static final int KEY_HEIGHT = 16;

    public QuestKey(int x, int y) {
        super(x, y, GameObjectType.QUEST_KEY);
        this.isTaken = false;
        this.setHasCollision(true);
        loadImages();
    }
    private void loadImages() {
        try {
            URL keyURL = getClass().getResource("/QuestKey.png");
            keyImage = ImageIO.read(keyURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getWidth() {
        return KEY_WIDTH;
    }

    @Override
    public int getHeight() {
        return KEY_HEIGHT;
    }


    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(keyImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        return false;
    }
}
