package Core.Model;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NPC extends GameObject {
    private Image npcImage;
    private boolean isTalking = false;
    private Image task0_1;
    private Image task0_2;
    private Image task1;
    private Image taskCompleted;
    private static final int NPC_WIDTH = 17;
    private static final int NPC_HEIGHT = 17;
    protected boolean task1Complete = false;
    protected boolean task0_2Complete = false;
    protected boolean task0_1Complete = false;

    public NPC(int x, int y) {
        super(x, y, GameObjectType.NPC);
        loadImages();
    }

    private void loadImages() {
        try {
            npcImage = ImageIO.read(getClass().getResource("/npcStand.png"));
            task0_1 = ImageIO.read(getClass().getResource("/Task_01.png"));
            task0_2 = ImageIO.read(getClass().getResource("/Task_02.png"));
            task1 = ImageIO.read(getClass().getResource("/Task1.png"));
            taskCompleted = ImageIO.read(getClass().getResource("/TaskFinish.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getWidth() {
        return NPC_WIDTH;
    }

    @Override
    public int getHeight() {
        return NPC_HEIGHT;
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(npcImage, x, y, null);

    }



    public void showFirstDialog(Graphics g) {
        isTalking = true;
        g.drawImage(task0_1, x, y  , null);
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



    public boolean isTask0_1Complete() {
        return task0_1Complete;
    }

    public boolean isTask1Complete() {
        return task1Complete;
    }

    public boolean isTask0_2Complete() {
        return task0_2Complete;
    }

    public void setTask0_1Complete(boolean task0_1Complete) {
        this.task0_1Complete = task0_1Complete;
    }

    public void setTask0_2Complete(boolean task0_2Complete) {
        this.task0_2Complete = task0_2Complete;
    }

    public void setTask1Complete(boolean task1Complete) {
        this.task1Complete = task1Complete;
    }

    public void hideDialog(Graphics graphics) {
        isTalking = false;
    }

    public void setTalking(boolean b) {
    }
}
