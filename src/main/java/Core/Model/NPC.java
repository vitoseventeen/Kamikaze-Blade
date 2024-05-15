package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class NPC extends GameObject {
    private Image npcImage;
    private boolean isTalking = false;

     public NPC(int x, int y) {
        super(x, y, GameObjectType.NPC);
        loadImages();
     }

    private void loadImages() {
        try {
            npcImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/npcStand.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(npcImage, x, y, null);
    }

    public void drawTalking(Graphics g, int x, int y) {
        g.drawImage(npcImage, x, y, null);
        g.setColor(Color.BLACK);
        g.fillRect(x, y - 20, 100, 20);
        g.setColor(Color.WHITE);
        g.drawString("Hello, im NPC", x + 10, y - 5);
    }

    @Override
    public boolean interact(Player player) {
        if (!isTalking) {
            isTalking = true;
            return true;
        }
        return false;
    }

    public boolean isTalking() {
        return isTalking;
    }
}
