package Core.Controller;

import Core.Model.Level;
import Core.Model.Player;
import Core.Model.Enemy;
import Core.Model.Tile;
import Core.View.Panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Core.Util.Constants.ATTACK_RADIUS;

public class Controller {
    private Player player;
    private Panel panel;
    private Level level;
    private List<Enemy> enemies;
    private GameManager gameManager;


    public Controller(Player player, Panel panel, Level level, List<Enemy> enemies, GameManager gameManager) {
        this.player = player;
        this.panel = panel;
        this.level = level;
        this.enemies = enemies;
        this.gameManager = gameManager;
    }


    // Check for collision with level boundaries and tiles
    boolean isCollision(int x, int y, int width, int height) {
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

        return false;
    }

    // Perform player attack action
    public void attack() {
        player.setAnimationType(Player.AnimationType.ATTACK);

        List<Enemy> nearbyEnemies = getNearbyEnemies(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for (Enemy enemy : nearbyEnemies) {
            enemy.takeDamage(1);
        }

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

    // Update player movement
    public void updatePlayerMovement(int deltaX, int deltaY) {
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

                panel.repaint();
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
        Random random = new Random();
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) {
                // Если враг мертв, он не должен двигаться
                enemy.setDx(0);
                enemy.setDy(0);
                enemy.setAnimationType(Enemy.AnimationType.DEATH);
                continue;
            }

            if (isPlayerInEnemyRadius(enemy, ATTACK_RADIUS)) { // Проверяем, находится ли игрок в радиусе 30 пикселей от врага
                int playerX = player.getX();
                int playerY = player.getY();
                int enemyX = enemy.getX();
                int enemyY = enemy.getY();

                // Определяем направление движения врага к игроку
                int dx = Integer.compare(playerX, enemyX);
                int dy = Integer.compare(playerY, enemyY);

                // Устанавливаем направление движения врага к игроку
                enemy.setDx(dx * enemy.getSpeed());
                enemy.setDy(dy * enemy.getSpeed());
                enemy.setAnimationType(Enemy.AnimationType.WALK);
            } else {
                // Если игрок не в радиусе видимости врага, враги двигаются случайным образом
                if (random.nextInt(100) < 5) { // 5% шанс изменить направление
                    int direction = random.nextInt(4);
                    switch (direction) {
                        case 0: // Движение вверх
                            enemy.setDy(-enemy.getSpeed());
                            enemy.setDx(0);
                            enemy.setDirection(Enemy.Direction.DOWN);
                            break;
                        case 1: // Движение вниз
                            enemy.setDy(enemy.getSpeed());
                            enemy.setDx(0);
                            enemy.setDirection(Enemy.Direction.UP);
                            break;
                        case 2: // Движение влево
                            enemy.setDx(-enemy.getSpeed());
                            enemy.setDy(0);
                            enemy.setDirection(Enemy.Direction.LEFT);
                            break;
                        case 3: // Движение вправо
                            enemy.setDx(enemy.getSpeed());
                            enemy.setDy(0);
                            enemy.setDirection(Enemy.Direction.RIGHT);
                            break;
                    }
                    enemy.setAnimationType(Enemy.AnimationType.WALK);
                }
            }

            // Перемещаем врага
            int newX = enemy.getX() + enemy.getDx();
            int newY = enemy.getY() + enemy.getDy();

            // Проверяем коллизии с игроком
            if (player.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                // Если враг касается игрока, атакуем игрока
                enemy.setAnimationType(Enemy.AnimationType.ATTACK);
                player.takeDamage(1);
            }

            // Проверяем коллизии с другими врагами
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

            // Проверяем коллизии с уровнем
            if (!isCollision(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                enemy.setX(newX);
                enemy.setY(newY);
            }

            // Небольшая задержка для плавности анимации
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Обновляем отрисовку
        panel.repaint();
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
}
