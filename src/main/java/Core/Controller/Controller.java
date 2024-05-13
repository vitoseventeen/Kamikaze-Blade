package Core.Controller;

import Core.Model.*;
import Core.View.GamePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Core.Util.Constants.ATTACK_RADIUS;

public class Controller {
    private Player player;
    private GamePanel gamePanel;
    private Level level;
    private List<Enemy> enemies;
    private GameManager gameManager;


    public Controller(Player player, GamePanel gamePanel, Level level, List<Enemy> enemies, GameManager gameManager) {
        this.player = player;
        this.gamePanel = gamePanel;
        this.level = level;
        this.enemies = enemies;
        this.gameManager = gameManager;
    }


    // Check for collision with level boundaries and tiles
    public boolean isCollision(int x, int y, int width, int height) {
        int TILE_SIZE = Core.Util.Constants.TILE_SIZE;

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

        for (GameObject object : gamePanel.getObjects()) {
            if (object.checkCollision(x, y, width, height)) {
                return true;
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy.checkCollision(x, y, width, height)) {
                return true;
            }
        }

        return false;
    }



    // Perform player attack action
    public void attack() {
        if (gameManager.isPaused() || player.isDead()) {
            return;
        }
        player.setAnimationType(Player.AnimationType.ATTACK);

        List<Enemy> nearbyEnemies = getNearbyEnemies(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for (Enemy enemy : nearbyEnemies) {
            if (!enemy.isDead()) {
                enemy.takeDamage(1);
            }
        }
        gamePanel.repaint();

        new Thread(() -> {
            try {
                Thread.sleep(200);
                player.setAnimationType(Player.AnimationType.IDLE);
                gamePanel.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }



    // Update player movement
    public void updatePlayerMovement(int deltaX, int deltaY) {
        if (player.isDead()) {
            return;
        }
        if (gameManager.isPaused()) {
            return;
        }
        int newX = player.getX() + deltaX;
        int newY = player.getY() + deltaY;

        // Check collision with enemies
        for (Enemy enemy : enemies) {
            if (!enemy.isDead() && enemy.checkCollisionWithEnemy(newX, newY, player.getWidth(), player.getHeight())) {
                return;
            }
        }

        // Check collision with level
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

            //check collision with panel objects


            gamePanel.repaint();
        }
    }

    public boolean isPlayerInEnemyRadius(Enemy enemy, int radius) {
        int playerX = player.getX();
        int playerY = player.getY();
        int enemyX = enemy.getX();
        int enemyY = enemy.getY();
        int distance = (int) Math.sqrt(Math.pow(playerX - enemyX, 2) + Math.pow(playerY - enemyY, 2));
        return distance <= radius;
    }


    // Move enemies on the game board
    public void moveEnemies() {
        if (player.isDead()) {
            return;
        }
        Random random = new Random();
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) {
                // If the enemy is dead, it shouldn't move
                enemy.setDx(0);
                enemy.setDy(0);
                enemy.setAnimationType(Enemy.AnimationType.DEATH);
                continue;
            }

            if (isPlayerInEnemyRadius(enemy, ATTACK_RADIUS)) {
                // If the player is within the enemy's attack radius
                int playerX = player.getX();
                int playerY = player.getY();
                int enemyX = enemy.getX();
                int enemyY = enemy.getY();

                int dx = Integer.compare(playerX, enemyX);
                int dy = Integer.compare(playerY, enemyY);

                enemy.setDx(dx * enemy.getSpeed());
                enemy.setDy(dy * enemy.getSpeed());
                enemy.setAnimationType(Enemy.AnimationType.WALK);
            } else {
                // If the player is not in the enemy's attack radius
                if (random.nextInt(100) < 5) { // 5% chance to change direction
                    int direction = random.nextInt(4);
                    switch (direction) {
                        case 0: // Move up
                            enemy.setDy(-enemy.getSpeed());
                            enemy.setDx(0);
                            enemy.setDirection(Enemy.Direction.UP);
                            break;
                        case 1: // Move down
                            enemy.setDy(enemy.getSpeed());
                            enemy.setDx(0);
                            enemy.setDirection(Enemy.Direction.DOWN);
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
            }

            // Move the enemy
            int newX = enemy.getX() + enemy.getDx();
            int newY = enemy.getY() + enemy.getDy();

            if (!isCollision(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                // Check collision with the player
                if (player.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                    if (enemy.canAttack()) {
                        enemy.setAnimationType(Enemy.AnimationType.ATTACK);
                        player.takeDamage(1);

                        enemy.setLastAttackTime(System.currentTimeMillis());
                        try {
                            Thread.sleep(200); // Adjust the time as needed
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        enemy.setAnimationType(Enemy.AnimationType.IDLE);
                    }
                } else {
                    // Check collision with other enemies
                    boolean collisionWithOtherEnemy = false;
                    for (Enemy otherEnemy : enemies) {
                        if (otherEnemy != enemy && otherEnemy.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                            collisionWithOtherEnemy = true;
                            break;
                        }
                    }
                    if (!collisionWithOtherEnemy) {
                        enemy.setX(newX);
                        enemy.setY(newY);
                    }
                }
            }

// Set the direction based on movement
            if (enemy.getDx() < 0) {
                enemy.setDirection(Enemy.Direction.LEFT);
            } else if (enemy.getDx() > 0) {
                enemy.setDirection(Enemy.Direction.RIGHT);
            } else if (enemy.getDy() < 0) {
                enemy.setDirection(Enemy.Direction.DOWN);
            } else if (enemy.getDy() > 0) {
                enemy.setDirection(Enemy.Direction.UP);
            }


            // Check collision with the player
            if (player.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                if (enemy.canAttack()) {
                    enemy.setAnimationType(Enemy.AnimationType.ATTACK);
                    player.takeDamage(1);
                    enemy.setLastAttackTime(System.currentTimeMillis());
                }
            }

            // Check collision with other enemies
            boolean collisionWithOtherEnemy = false;
            for (Enemy otherEnemy : enemies) {
                if (otherEnemy != enemy && otherEnemy.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                    collisionWithOtherEnemy = true;
                    break;
                }
            }
            if (collisionWithOtherEnemy) {
                continue;
            }


            // Small delay for smooth animation
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Update the rendering
        gamePanel.repaint();
    }




    // Get enemies within a specified range
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


    public void togglePause() {
        gameManager.togglePause();
    }

    public void interact( ) {

    }
}
