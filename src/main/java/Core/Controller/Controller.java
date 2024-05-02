package Core.Controller;

import Core.Model.Level;
import Core.Model.Player;
import Core.Model.Enemy;
import Core.Model.Tile;
import Core.View.Panel;

import static Core.Util.Constants.TILE_SIZE;

import java.util.List;

public class Controller {
    private Player player;
    private Panel panel;
    private Level level;
    private List<Enemy> enemies;

    public Controller(Player player, Panel panel, Level level, List<Enemy> enemies) {
        this.player = player;
        this.panel = panel;
        this.level = level;
        this.enemies = enemies;
    }

    boolean isCollision(int x, int y, int width, int height) {
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

            if (deltaX < 0) {
                player.setDirection(Player.Direction.LEFT);
            } else if (deltaX > 0) {
                player.setDirection(Player.Direction.RIGHT);
            } else if (deltaY < 0) {
                player.setDirection(Player.Direction.DOWN);
            } else if (deltaY > 0) {
                player.setDirection(Player.Direction.UP);
            }

            player.setAnimationType(Player.AnimationType.WALK);

            panel.repaint();
        }
    }

    public void attack() {
        player.setAnimationType(Player.AnimationType.ATTACK);
        panel.repaint();

        new Thread(() -> {
            try {
                Thread.sleep(100);
                player.setAnimationType(Player.AnimationType.IDLE);
                panel.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void updateGame() {


        for (Enemy enemy : enemies) {
            // Обновить состояние врага
            // Например, переместить врага, если это нужно
        }

        panel.repaint();
    }

    public Player getPlayer() {
        return player;
    }
}
