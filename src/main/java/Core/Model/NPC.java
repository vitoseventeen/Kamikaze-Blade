package Core.Model;

import Core.Model.GameObject;
import Core.Model.GameObjectType;
import Core.Model.Player;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NPC extends GameObject {
    private Image npcImage;
    private boolean isTalking = false;
    private Image task1;
    private Image taskCompleted;
    private static final int NPC_WIDTH = 17;
    private static final int NPC_HEIGHT = 17;
    private boolean task1Complete = false;

    public NPC(int x, int y) {
        super(x, y, GameObjectType.NPC);
        loadImages();
    }

    private void loadImages() {
        try {
            npcImage = ImageIO.read(getClass().getResource("/npcStand.png"));
            task1 = ImageIO.read(getClass().getResource("/Task1.png"));
            taskCompleted = ImageIO.read(getClass().getResource("/TaskFinish.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(npcImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        if (!task1Complete && !isTalking) {
            isTalking = true;
            return true;
        }
        return false;
    }

    public boolean isTalking() {
        return isTalking;
    }

    public Image getTask1() {
        return task1;
    }

    public Image getTaskCompleted() {
        return taskCompleted;
    }
    public boolean isTask1Complete() {
        return task1Complete;
    }

    public void setTask1Complete(boolean complete) {
        this.task1Complete = complete;
    }
}
