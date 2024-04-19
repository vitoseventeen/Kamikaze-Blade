package Core.Controller;

import Core.Model.Level;
import Core.Model.LevelManager;
import Core.Model.Player;
import Core.Model.Tile;
import Core.View.Panel;


import static Core.Util.Constants.TILE_SIZE;

public class Controller {
    private Player player;
    private Panel panel;
    private LevelManager levelManager;
    private Level level;

    public Controller(Player player, Panel panel, LevelManager levelManager, Level level) {
        this.player = player;
        this.panel = panel;
        this.levelManager = levelManager;
        this.level = level;
    }

    private boolean isCollision(int x, int y, int width, int height) {
        int tileX = x / TILE_SIZE;
        int tileY = y / TILE_SIZE;
        int tileWidth = (x + width) / TILE_SIZE;
        int tileHeight = (y + height) / TILE_SIZE;

        for (int i = tileX; i <= tileWidth; i++) {
            for (int j = tileY; j <= tileHeight; j++) {
                Tile tile = level.getTile(i, j);
                if (tile.hasCollision()) {
                    return true;
                }
            }
        }

        return false;
    }


    public void moveLeft() {
        int newX = player.getX() - player.getSpeed();
        if (!isCollision(newX, player.getY(), player.getWidth(), player.getHeight())) {
            player.setX(newX);
        }
        panel.repaint();
    }

    public void moveRight() {
        int newX = player.getX() + player.getSpeed();
        if (!isCollision(newX, player.getY(), player.getWidth(), player.getHeight())) {
            player.setX(newX);
        }
        panel.repaint();
    }

    public void moveUp() {
        int newY = player.getY() - player.getSpeed();
        if (!isCollision(player.getX(), newY, player.getWidth(), player.getHeight())) {
            player.setY(newY);
        }
        panel.repaint();
    }

    public void moveDown() {
        int newY = player.getY() + player.getSpeed();
        if (!isCollision(player.getX(), newY, player.getWidth(), player.getHeight())) {
            player.setY(newY);
        }
        panel.repaint();
    }
}
