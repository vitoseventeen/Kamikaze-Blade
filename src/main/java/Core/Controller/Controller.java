package Core.Controller;

import Core.Model.*;
import Core.View.GamePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static Core.Util.Constants.*;

public class Controller {
    private Player player;
    private GamePanel gamePanel;
    private Level level;
    private List<Enemy> enemies;
    private GameManager gameManager;
    private Inventory inventory;
    private int COLLISION_RADIUS = 1;


    public Controller(Player player, GamePanel gamePanel, Level level, List<Enemy> enemies, GameManager gameManager) {
        this.player = player;
        this.gamePanel = gamePanel;
        this.level = level;
        this.enemies = enemies;
        this.gameManager = gameManager;
        this.inventory = player.getInventory();
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

        // Check collision with objects
        for (GameObject object : gamePanel.getObjects()) {
            // Проверяем, имеет ли объект коллизию
            if (object.hasCollision() && object.checkCollision(x, y, width, height)) {
                return true;
            }
            if (object.getType().equals(GameObjectType.DOOR)) {
                Door door = (Door) object;
                if (!door.isOpened()) {
                    int doorCollisionX = Integer.parseInt(door.getX()) - COLLISION_RADIUS;
                    int doorCollisionY = Integer.parseInt(door.getY()) - COLLISION_RADIUS;
                    int doorCollisionWidth = door.getWidth() + 2 * COLLISION_RADIUS;
                    int doorCollisionHeight = door.getHeight() + 2 * COLLISION_RADIUS;
                    if (x + width > doorCollisionX && x < doorCollisionX + doorCollisionWidth &&
                            y + height > doorCollisionY && y < doorCollisionY + doorCollisionHeight) {
                        return true;
                    }

                }
            } if (object.getType().equals(GameObjectType.LEVELDOOR)) {
                LevelDoor levelDoor = (LevelDoor) object;
                if (!levelDoor.isOpened()) {
                    int levelDoorCollisionX = Integer.parseInt(levelDoor.getX()) - COLLISION_RADIUS;
                    int levelDoorCollisionY = Integer.parseInt(levelDoor.getY()) - COLLISION_RADIUS;
                    int levelDoorCollisionWidth = levelDoor.getWidth() + 2 * COLLISION_RADIUS;
                    int levelDoorCollisionHeight = levelDoor.getHeight() + 2 * COLLISION_RADIUS;
                    if (x + width > levelDoorCollisionX && x < levelDoorCollisionX + levelDoorCollisionWidth &&
                            y + height > levelDoorCollisionY && y < levelDoorCollisionY + levelDoorCollisionHeight) {
                        return true;
                    }
                }
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
            if (enemy.isDead()) {
                player.addScore();
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

        checkLevelTeleport(newX,newY);

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
            gamePanel.repaint();
        }
    }

    private void checkLevelTeleport(int newX, int newY) {
        for (GameObject object : gamePanel.getObjects()) {
            if (object.getType().equals(GameObjectType.LEVELDOOR)) {
                LevelDoor levelDoor = (LevelDoor) object;
                if (levelDoor.checkCollision(newX, newY, player.getWidth(), player.getHeight())) {
                    if (levelDoor.isOpened()) {
                        levelDoor.interact(player);
                        gamePanel.repaint();
                    }
                }
            }
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

    public void interact() {
        if (gameManager.isPaused() || player.isDead()) {
            return;
        }

        int playerCenterX = player.getX() + player.getWidth() / 2;
        int playerCenterY = player.getY() + player.getHeight() / 2;

        for (GameObject object : gamePanel.getObjects()) {

            if (object.getType().equals(GameObjectType.DOOR)) {
                Door door = (Door) object;
                int doorCenterX = Integer.parseInt(door.getX()) + door.getWidth() / 2;
                int doorCenterY = Integer.parseInt(door.getY()) + door.getHeight() / 2;

                double distance = Math.sqrt(Math.pow(playerCenterX - doorCenterX, 2) + Math.pow(playerCenterY - doorCenterY, 2));

                // Учтем радиус коллизии при взаимодействии с дверью
                if (distance <= INTERACTION_RADIUS + (COLLISION_RADIUS + 20 )) {
                    if (door.interact(player)) {
                        COLLISION_RADIUS = 0;
                        gamePanel.repaint();
                        gamePanel.removeObject(object);
                    }
                    return;
                }

            }
            if (object.getType().equals(GameObjectType.LEVELDOOR)) {
                LevelDoor level_door = (LevelDoor) object;
                int levelDoorCenterX = Integer.parseInt(level_door.getX()) + level_door.getWidth() / 2;
                int levelDoorCenterY = Integer.parseInt(level_door.getY()) + level_door.getHeight() / 2;

                double distance = Math.sqrt(Math.pow(playerCenterX - levelDoorCenterX, 2) + Math.pow(playerCenterY - levelDoorCenterY, 2));
                if (distance <= INTERACTION_RADIUS + (COLLISION_RADIUS + 20 )) {
                    if (level_door.interact(player)) {
                        COLLISION_RADIUS = 0;
                        gamePanel.repaint();
                        gamePanel.removeObject(object);
                    }
                    return;
                }
            }

            if (object.getType().equals(GameObjectType.NPC)) {
                NPC npc = (NPC) object;
                int npcCenterX = Integer.parseInt(npc.getX()) + npc.getWidth() / 2;
                int npcCenterY = Integer.parseInt(npc.getY()) + npc.getHeight() / 2;

                double distance = Math.sqrt(Math.pow(playerCenterX - npcCenterX, 2) + Math.pow(playerCenterY - npcCenterY, 2));

                if (distance <= INTERACTION_RADIUS + (COLLISION_RADIUS + 5)) {
                    npc.interact(player);
                    if (inventory.isQuestFinished() && !inventory.isFull()) {
                        player.getInventory().removeCoinFromBalance(3);
                        player.getInventory().addItem(new QuestKey(0, 0));
                        npc.setTask1Complete(true);
                    }
                    gamePanel.repaint();
                    return;
                }
        }


            int objectCenterX = Integer.parseInt(object.getX()) + object.getWidth() / 2;
            int objectCenterY = Integer.parseInt(object.getY()) + object.getHeight() / 2;

            double distance = Math.sqrt(Math.pow(playerCenterX - objectCenterX, 2) + Math.pow(playerCenterY - objectCenterY, 2));

            if (distance <= INTERACTION_RADIUS) {
                object.interact(player);
                if (object.getType().equals(GameObjectType.CHEST)) {
                    Chest chest = (Chest) object;
                    chest.interact(player);
                    player.setAnimationType(Player.AnimationType.OPEN); // TODO: MAKE GOOD ANIMATION
                    if (chest.isOpened()) {
                        gamePanel.repaint();
                    }
                }
                if (object.getType().equals(GameObjectType.KEY)) {
                    Key key = (Key) object;
                    if (inventory.isFull()) {
                        System.out.println("Inventory is full");

                        return;
                    }
                    key.interact(player);
                    inventory.printInventory();
                    gamePanel.repaint();
                    gamePanel.removeObject(object);
                    // remove collision with key
                }
                if (object.getType().equals(GameObjectType.COIN)) {
                    Coin coin = (Coin) object;
                    coin.interact(player);
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                }
                if (object.getType().equals(GameObjectType.POTION)) {

                    Potion potion = (Potion) object;
                    if (inventory.isFull()) {
                        System.out.println("Inventory is full");

                        return;
                    }
                    potion.interact(player);
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                }
                if (object.getType().equals(GameObjectType.HEAL)) {
                    Heal heal = (Heal) object;
                    if (inventory.isFull()) {
                        System.out.println("Inventory is full");

                        return;
                    }
                    heal.interact(player);
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                }
                if (object.getType().equals(GameObjectType.QUEST_KEY)) {
                    QuestKey questKey = (QuestKey) object;
                    if (inventory.isFull()) {
                        System.out.println("Inventory is full");
                        return;
                    }
                    questKey.interact(player);
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                }
                else {
                    player.setAnimationType(Player.AnimationType.INTERACT);
                }
                gamePanel.repaint();
                break;
            }
        }
    }

    public void craftHeal() {
        int potionCount = 0;
        List<GameObject> potionsToRemove = new ArrayList<>();
        for (GameObject item : inventory.getItems()) {
            if (item.getType().equals(GameObjectType.POTION)) {
                potionsToRemove.add(item);
                potionCount++;
                if (potionCount == 2) {
                    break;
                }
            }
        }

        if (potionCount == 2) {
            for (GameObject potion : potionsToRemove) {
                inventory.removeItem(potion);
            }
            inventory.addItem(new Heal(0, 0));
        }
        gamePanel.repaint();
    }


    public void showInventory() {
        gameManager.showInventoryMenu();
    }
    public boolean showingInventory() {
        return  gameManager.showingInventory();
    }


    public void hideInventory() {
        gameManager.hideInventoryMenu();
    }
}
