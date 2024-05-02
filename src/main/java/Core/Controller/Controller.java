// В классе Controller
package Core.Controller;

import Core.Model.Level;
import Core.Model.Player;
import Core.Model.Enemy;
import Core.Model.Tile;
import Core.View.Panel;

import static Core.Util.Constants.TILE_SIZE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if (x < 0 || y < 0 || x + width > level.getWidth() * TILE_SIZE || y + height > level.getHeight() * TILE_SIZE) {
            return true;
        }

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

    public void attack() {
        player.setAnimationType(Player.AnimationType.ATTACK);
        panel.repaint();

        new Thread(() -> {
            try {
                Thread.sleep(200);
                player.setAnimationType(Player.AnimationType.IDLE);
                panel.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void movePlayer(int deltaX, int deltaY) {
        int newX = player.getX() + deltaX;
        int newY = player.getY() + deltaY;

        // Check for collision with enemies
        for (Enemy enemy : enemies) {
            if (isCollision(newX, newY, player.getWidth(), player.getHeight())) {
                return;
            }
        }

        // Check for collision with level
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

            if (player.getAnimationType() != Player.AnimationType.ATTACK) {
                player.setAnimationType(Player.AnimationType.WALK);
            }


            panel.repaint();
        }
    }

    public void moveEnemies() {
        // Update enemies
        Random random = new Random();
        for (Enemy enemy : enemies) {
            if (random.nextInt(100) < 5) { // 5% chance to change direction
                int direction = random.nextInt(4);
                switch (direction) {
                    case 0: // Move up
                        enemy.setDy(-enemy.getSpeed());
                        enemy.setDx(0);
                        enemy.setDirection(Enemy.Direction.DOWN);
                        break;
                    case 1: // Move down
                        enemy.setDy(enemy.getSpeed());
                        enemy.setDx(0);
                        enemy.setDirection(Enemy.Direction.UP);
                        break;
                    case 2: // Move left
                        enemy.setDx(-enemy.getSpeed());
                        enemy.setDy(0);
                        enemy.setDirection(Enemy.Direction.LEFT);
                        break;
                    case 3: // Move right
                        enemy.setDx(enemy.getSpeed());
                        enemy.setDy(0);
                        enemy.setDirection(Enemy.Direction.RIGHT);
                        break;
                }
                enemy.setAnimationType(Enemy.AnimationType.WALK);
            }

            int newX = enemy.getX() + enemy.getDx();
            int newY = enemy.getY() + enemy.getDy();

            // Check for collision with player
            if (player.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                continue;
            }

            // Check for collision with other enemies
            boolean collisionWithOtherEnemy = false;
            for (Enemy otherEnemy : enemies) {
                if (otherEnemy != enemy && isCollision(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                    collisionWithOtherEnemy = true;
                    break;
                }
            }
            if (collisionWithOtherEnemy) {
                continue;
            }

            // Check for collision with level
            if (!isCollision(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                enemy.setX(newX);
                enemy.setY(newY);
            }

            // small delay
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        panel.repaint();
    }

    public List<Enemy> getNearbyEnemies(int x, int y, int width, int height) {
        List<Enemy> nearbyEnemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (Math.abs(enemy.getX() - x) <= width && Math.abs(enemy.getY() - y) <= height) {
                nearbyEnemies.add(enemy);
            }
        }
        return nearbyEnemies;
    }


    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }
}
