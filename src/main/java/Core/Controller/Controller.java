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

    public void movePlayer(int deltaX, int deltaY) {
        int newX = player.getX() + deltaX;
        int newY = player.getY() + deltaY;

        if (!isCollision(newX, newY, player.getWidth(), player.getHeight())) {
            player.setX(newX);
            player.setY(newY);
            panel.repaint();
        }
    }

    public void moveLeft() {
        movePlayer(-player.getSpeed(), 0);
    }

    public void moveRight() {
        movePlayer(player.getSpeed(), 0);
    }

    public void moveUp() {
        movePlayer(0, -player.getSpeed());
    }

    public void moveDown() {
        movePlayer(0, player.getSpeed());
    }

    public Player getPlayer() {
        return player;
    }
}
