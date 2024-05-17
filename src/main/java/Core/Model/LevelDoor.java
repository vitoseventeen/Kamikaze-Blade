package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class LevelDoor extends GameObject{

    private boolean isOpened = false;
    private Image closedLevelDoorImage;
    private Image openedLevelDoorImage;
    private static final int LEVEL_DOOR_WIDTH = 64;
    private static final int LEVEL_DOOR_HEIGHT = 64;

    public LevelDoor(int x, int y) {
        super(x, y, GameObjectType.LEVELDOOR);
        loadImages();
        this.setHasCollision(true);
    }

    @Override
    public int getWidth() {
        return LEVEL_DOOR_WIDTH;
    }

    @Override
    public int getHeight() {
        return LEVEL_DOOR_HEIGHT;
    }

    private void loadImages() {
        try {
            URL closedLevelDoorURL = getClass().getResource("/levelDoorClosed.png");
            URL openedLevelDoorURL = getClass().getResource("/levelDoorOpened.png");
            closedLevelDoorImage = ImageIO.read(closedLevelDoorURL);
            openedLevelDoorImage = ImageIO.read(openedLevelDoorURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: ADD TO ALL CLASSES AND MAKE LEVEL DOORS INTERACTABLE (еще переход между лвлами)

    public boolean isOpened() {
        this.setHasCollision(false);
        return isOpened;
    }

    public void open() {
        isOpened = true;

    }

    public void drawOpened(Graphics g, int x, int y) {
        g.drawImage(openedLevelDoorImage, x, y, null);
    }


    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(closedLevelDoorImage, x, y, null);
    }

    @Override
    public boolean interact(Player player) {
        QuestKey questKey = (QuestKey) player.getInventory().getItem(GameObjectType.QUEST_KEY);
        if (!isOpened ) {
            if (player.getInventory().getItem(GameObjectType.QUEST_KEY) != null && !isOpened) {
                player.getInventory().removeItem(questKey);
                open();
            }
        }
        return false;

    }
}
