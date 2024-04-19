package Core.Controller;

import Core.Model.Level;
import Core.Model.LevelManager;
import Core.Model.Player;
import Core.View.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Controller {
    private Player player;
    private Panel panel;
    private LevelManager levelManager;

    public Controller(Player player, Panel panel, LevelManager levelManager) {
        this.player = player;
        this.panel = panel;
        this.levelManager = levelManager;
    }
    private boolean isCollision(int x, int y) {
        Level currentLevel = levelManager.getCurrentLevel();
        BufferedImage levelMask = currentLevel.getLevelMask();

        if (x < 0 || x >= levelMask.getWidth() || y < 0 || y >= levelMask.getHeight()) {
            return true;
        }

        int rgb = levelMask.getRGB(x, y);
        Color color = new Color(rgb);
        return color.equals(Color.BLACK);
    }


    public void moveLeft() {
        int newX = player.getX() - player.getSpeed();
        if (!isCollision(newX, player.getY())) {
            player.setX(newX);
            panel.repaint();
        }
    }

    public void moveRight() {
        int newX = player.getX() + player.getSpeed();
        if (!isCollision(newX, player.getY())) {
            player.setX(newX);
            panel.repaint();
        }
    }

    public void moveUp() {
        int newY = player.getY() - player.getSpeed();
        if (!isCollision(player.getX(), newY)) {
            player.setY(newY);
            panel.repaint();
        }
    }

    public void moveDown() {
        int newY = player.getY() + player.getSpeed();
        if (!isCollision(player.getX(), newY)) {
            player.setY(newY);
            panel.repaint();
        }
    }


}
