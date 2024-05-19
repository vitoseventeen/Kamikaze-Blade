package Model;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Represents a non-playable character (NPC) in the game.
 */
public class NPC extends GameObject {
    private Image npcImage, npcQuest;
    private boolean isTalking = false;
    private Image task1;
    private Image taskCompleted;
    private static final int NPC_WIDTH = 17;
    private static final int NPC_HEIGHT = 17;
    private boolean task1Complete = false;

    /**
     * Constructs an NPC object at the specified position.
     *
     * @param x the x-coordinate of the NPC
     * @param y the y-coordinate of the NPC
     */
    public NPC(int x, int y) {
        super(x, y, GameObjectType.NPC);
        loadImages();
    }

    /**
     * Loads images for the NPC.
     */
    private void loadImages() {
        try {
            npcImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/npcStand.png")));
            npcQuest = ImageIO.read(Objects.requireNonNull(getClass().getResource("/npcQuest.png")));
            task1 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Task1.png")));
            taskCompleted = ImageIO.read(Objects.requireNonNull(getClass().getResource("/TaskFinish.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the NPC image.
     *
     * @param g the graphics object to draw on
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     */
    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(npcImage, x, y, null);
    }

    /**
     * Draws the NPC image after a quest is completed.
     *
     * @param g the graphics object to draw on
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     */
    public void drawAfterQuest(Graphics g, int x, int y) {
        g.drawImage(npcQuest, x, y, null);
    }

    /**
     * Handles player interaction with the NPC.
     *
     * @param player the player interacting with the NPC
     * @return true if the NPC is available for interaction, false otherwise
     */
    @Override
    public boolean interact(Player player) {
        if (!task1Complete && !isTalking) {
            isTalking = true;
            return true;
        }
        return false;
    }

    /**
     * Checks if the NPC is currently engaged in conversation.
     *
     * @return true if the NPC is talking, false otherwise
     */
    public boolean isTalking() {
        return isTalking;
    }

    /**
     * Retrieves the image representing a task given by the NPC.
     *
     * @return the image representing the task
     */
    public Image getTask1() {
        return task1;
    }

    /**
     * Retrieves the image representing a completed task given by the NPC.
     *
     * @return the image representing the completed task
     */
    public Image getTaskCompleted() {
        return taskCompleted;
    }

    /**
     * Checks if the first task given by the NPC is completed.
     *
     * @return true if the task is completed, false otherwise
     */
    public boolean isTask1Complete() {
        return task1Complete;
    }

    /**
     * Sets the completion status of the first task given by the NPC.
     *
     * @param complete true if the task is completed, false otherwise
     */
    public void setTask1Complete(boolean complete) {
        this.task1Complete = complete;
    }
}
