package Controller;


import Model.*;
import Util.Constants;
import View.GamePanel;

import java.util.*;
import java.util.logging.Logger;

import static Util.Constants.*;

/**
 * The Controller class manages the game logic, including player and enemy movements, collision detection, and level transitions.
 */
public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    private final Player player;
    private final GamePanel gamePanel;
    private Level level;
    private final List<Enemy> enemies;
    private final GameManager gameManager;
    private final Inventory inventory;
    private int radiusOfCollision = 1;
    private boolean isLevelChanged = false;
    private long lastUpdateTime = System.currentTimeMillis();

    /**
     * Constructs a Controller with the specified player, game panel, level, enemies, and game manager.
     *
     * @param player      the player
     * @param gamePanel   the game panel
     * @param level       the level
     * @param enemies     the list of enemies
     * @param gameManager the game manager
     */
    public Controller(Player player, GamePanel gamePanel, Level level, List<Enemy> enemies, GameManager gameManager) {
        this.player = player;
        this.gamePanel = gamePanel;
        this.level = level;
        this.enemies = enemies;
        this.gameManager = gameManager;
        this.inventory = player.getInventory();
    }

    /**
     * Spawns enemies at random positions within the level.
     */
    protected synchronized void spawnEnemies() {
        enemies.clear();
        Random random = new Random();
        for (int i = 0; i < Constants.NUMBER_OF_ENEMIES; i++) {
            int x, y;
            do {
                x = random.nextInt(level.getWidth() * Constants.TILE_SIZE);
                y = random.nextInt(level.getHeight() * Constants.TILE_SIZE);
            } while (isCollision(x, y, PLAYER_WIDTH, PLAYER_HEIGHT));
            Enemy enemy = new Enemy("Enemy" + i, x, y, PLAYER_HEIGHT, PLAYER_WIDTH);
            enemies.add(enemy);
            logger.info("Spawned enemy at coordinates: (" + x + ", " + y + ")");
        }
    }

    /**
     * Checks for collisions at the specified coordinates.
     *
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param width  the width of the object
     * @param height the height of the object
     * @return true if a collision is detected, false otherwise
     */
    protected boolean isCollision(int x, int y, int width, int height) {
        int TILE_SIZE = Constants.TILE_SIZE;
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
            if (object.hasCollision() && object.checkCollision(x, y, width, height)) {
                return true;
            }
            if (object.getType().equals(GameObjectType.DOOR)) {
                Door door = (Door) object;
                if (!door.isOpened()) {
                    int doorCollisionX = Integer.parseInt(door.getX()) - radiusOfCollision;
                    int doorCollisionY = Integer.parseInt(door.getY()) - radiusOfCollision;
                    int doorCollisionWidth = DOOR_WIDTH + 2 * radiusOfCollision;
                    int doorCollisionHeight = DOOR_HEIGHT + 2 * radiusOfCollision;
                    if (x + width > doorCollisionX && x < doorCollisionX + doorCollisionWidth &&
                            y + height > doorCollisionY && y < doorCollisionY + doorCollisionHeight) {
                        return true;
                    }
                }
            }
            if (object.getType().equals(GameObjectType.LEVELDOOR)) {
                LevelDoor levelDoor = (LevelDoor) object;
                if (!levelDoor.isOpened()) {
                    int levelDoorCollisionX = Integer.parseInt(levelDoor.getX()) - radiusOfCollision;
                    int levelDoorCollisionY = Integer.parseInt(levelDoor.getY()) - radiusOfCollision;
                    int levelDoorCollisionWidth = DOOR_WIDTH + 2 * radiusOfCollision;
                    int levelDoorCollisionHeight = DOOR_HEIGHT + 2 * radiusOfCollision;
                    if (x + width > levelDoorCollisionX && x < levelDoorCollisionX + levelDoorCollisionWidth &&
                            y + height > levelDoorCollisionY && y < levelDoorCollisionY + levelDoorCollisionHeight) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Initiates the player's attack and handles damage to nearby enemies.
     */
    public void attack() {
        if (gameManager.isPaused() || player.isDead()) {
            return;
        }
        player.setAnimationType(Player.AnimationType.ATTACK);

        List<Enemy> nearbyEnemies = getNearbyEnemies(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for (Enemy enemy : nearbyEnemies) {
            enemy.takeDamage(1);
            if (enemy.getHealth() > 0 ) {
                logger.info(enemy.getName() + " took damage");
            } else if (enemy.getHealth() == 0 ) {
                player.addScore();
                logger.info(enemy.getName() + " is dead. Player score increased");
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


    /**
     * Updates the player's movement based on the specified delta values.
     *
     * @param deltaX the change in x-coordinate
     * @param deltaY the change in y-coordinate
     */
    public void updatePlayerMovement(int deltaX, int deltaY) {
        if (player.isDead() || gameManager.isPaused()) {
            return;
        }
        int newX = player.getX() + deltaX;
        int newY = player.getY() + deltaY;

        for (Enemy enemy : enemies) {
            if (!enemy.isDead() && enemy.checkCollisionWithEnemy(newX, newY, player.getWidth(), player.getHeight())) {
                return;
            }
        }

        if (level.getTile(newX / TILE_SIZE, newY / TILE_SIZE).getSurfaceType().equals(SurfaceType.LEVELTILE)) {
            loadNextLevel();
            logger.info("Player moved to next level.");
        }

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

    /**
     * Loads the next level and resets necessary game components.
     */
    private void loadNextLevel() {
        gamePanel.clearObjects();
        player.setX(500);
        player.setY(500);

        if (!isLevelChanged) {
            this.level = Level.loadLevelFromJson("level2.json");
            logger.info("Loading level 2.");
        } else {
            this.level = Level.loadLevelFromJson("level1.json");
            logger.info("Loading level 1.");
        }

        gamePanel.setLevel(level);
        isLevelChanged = !isLevelChanged;
        List<GameObject> newObjects = level.getObjects();
        for (GameObject object : newObjects) {
            gamePanel.addObject(object);
        }
        spawnEnemies();
        gamePanel.repaint();
    }

    /**
     * Checks if the player is within a specified radius of an enemy.
     *
     * @param enemy  the enemy
     * @param radius the radius to check
     * @return true if the player is within the radius, false otherwise
     */
    protected boolean isPlayerInEnemyRadius(Enemy enemy, int radius) {
        int playerX = player.getX();
        int playerY = player.getY();
        int enemyX = enemy.getX();
        int enemyY = enemy.getY();
        int distance = (int) Math.sqrt(Math.pow(playerX - enemyX, 2) + Math.pow(playerY - enemyY, 2));
        return distance <= radius;
    }

    /**
     * Moves the enemies based on their AI and the player's position.
     */
    protected synchronized void moveEnemies() {
        if (player.isDead()) {
            logger.info("Player is dead. Enemies will not move.");
            return;
        }
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastUpdateTime;
        if (elapsedTime >= MOVEMENT_DELAY) {
            Random random = new Random();
            for (Enemy enemy : enemies) {
                if (enemy.isDead()) {
                    enemy.setDx(0);
                    enemy.setDy(0);
                    enemy.setAnimationType(Enemy.AnimationType.DEATH);
                    continue;
                }
                if (isPlayerInEnemyRadius(enemy, ATTACK_RADIUS)) {
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
                    if (random.nextInt(100) < 5) {
                        int direction = random.nextInt(4);
                        switch (direction) {
                            case 0:
                                enemy.setDy(-enemy.getSpeed());
                                enemy.setDx(0);
                                enemy.setDirection(Enemy.Direction.UP);
                                break;
                            case 1:
                                enemy.setDy(enemy.getSpeed());
                                enemy.setDx(0);
                                enemy.setDirection(Enemy.Direction.DOWN);
                                break;
                            case 2:
                                enemy.setDx(-enemy.getSpeed());
                                enemy.setDy(0);
                                enemy.setDirection(Enemy.Direction.LEFT);
                                break;
                            case 3:
                                enemy.setDx(enemy.getSpeed());
                                enemy.setDy(0);
                                enemy.setDirection(Enemy.Direction.RIGHT);
                                break;
                        }
                        enemy.setAnimationType(Enemy.AnimationType.WALK);
                    }
                }

                int newX = enemy.getX() + enemy.getDx();
                int newY = enemy.getY() + enemy.getDy();

                if (!isCollision(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                    if (player.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                        if (enemy.canAttack()) {
                            enemy.setAnimationType(Enemy.AnimationType.ATTACK);
                            player.takeDamage(1);
                            logger.info( enemy.getName() + " attacked the player.");
                            enemy.setLastAttackTime(System.currentTimeMillis());
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            enemy.setAnimationType(Enemy.AnimationType.IDLE);
                        }
                    } else {
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

                if (enemy.getDx() < 0) {
                    enemy.setDirection(Enemy.Direction.LEFT);
                } else if (enemy.getDx() > 0) {
                    enemy.setDirection(Enemy.Direction.RIGHT);
                } else if (enemy.getDy() < 0) {
                    enemy.setDirection(Enemy.Direction.DOWN);
                } else if (enemy.getDy() > 0) {
                    enemy.setDirection(Enemy.Direction.UP);
                }

                if (player.checkCollisionWithEnemy(newX, newY, enemy.getWidth(), enemy.getHeight())) {
                    if (enemy.canAttack()) {
                        enemy.setAnimationType(Enemy.AnimationType.ATTACK);
                        player.takeDamage(1);
                        enemy.setLastAttackTime(System.currentTimeMillis());
                    }
                }

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
            }

            lastUpdateTime = currentTime;
        }

        gamePanel.repaint();
    }

    /**
     * Returns a list of nearby enemies within a specified radius of the player.
     *
     * @param x      the x-coordinate of the center point
     * @param y      the y-coordinate of the center point
     * @param width  the width of the area
     * @param height the height of the area
     * @return the list of nearby enemies
     */

    protected List<Enemy> getNearbyEnemies(int x, int y, int width, int height) {
        List<Enemy> nearbyEnemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (Math.abs(enemy.getX() - x) <= width && Math.abs(enemy.getY() - y) <= height) {
                nearbyEnemies.add(enemy);
            }
        }
        return nearbyEnemies;
    }


// ------------------------------------------------------------------------------------------------------------

    /**
     * Checks if the level has changed.
     *
     * @return true if the level has changed, false otherwise
     */
    protected boolean isLevelChanged() {
        return isLevelChanged;
    }

    /**
     * Returns the list of enemies.
     *
     * @return the list of enemies
     */
    protected List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the player.
     *
     * @return the player
     */
    protected Player getPlayer() {
        return player;
    }

    /**
     * Toggles the pause state of the game.
     */
    protected void togglePause() {
        gameManager.togglePause();
        logger.info("Game pause state toggled.");
    }

    /**
     * Handles interactions between the player and game objects.
     */
    public void interact() {
        if (gameManager.isPaused() || player.isDead()) {
            return;
        }

        int playerCenterX = player.getX() + player.getWidth() / 2;
        int playerCenterY = player.getY() + player.getHeight() / 2;

        for (GameObject object : gamePanel.getObjects()) {
            if (object.getType().equals(GameObjectType.DOOR)) {
                Door door = (Door) object;
                int doorCenterX = Integer.parseInt(door.getX()) + DOOR_WIDTH / 2;
                int doorCenterY = Integer.parseInt(door.getY()) + DOOR_HEIGHT / 2;

                double distance = Math.sqrt(Math.pow(playerCenterX - doorCenterX, 2) + Math.pow(playerCenterY - doorCenterY, 2));

                if (distance <= INTERACTION_RADIUS + (radiusOfCollision + 20)) {
                    if (door.interact(player)) {
                        radiusOfCollision = 0;
                        gamePanel.repaint();
                        gamePanel.removeObject(object);
                        logger.info("Player interacted with door at coordinates: (" + doorCenterX + ", " + doorCenterY + ")");
                    }
                    return;
                }
            }
            if (object.getType().equals(GameObjectType.LEVELDOOR)) {
                LevelDoor levelDoor = (LevelDoor) object;
                int levelDoorCenterX = Integer.parseInt(levelDoor.getX()) + DOOR_WIDTH / 2;
                int levelDoorCenterY = Integer.parseInt(levelDoor.getY()) + DOOR_HEIGHT / 2;

                double distance = Math.sqrt(Math.pow(playerCenterX - levelDoorCenterX, 2) + Math.pow(playerCenterY - levelDoorCenterY, 2));
                if (distance <= INTERACTION_RADIUS + (radiusOfCollision + 20)) {
                    if (levelDoor.interact(player)) {
                        radiusOfCollision = 0;
                        gamePanel.repaint();
                        gamePanel.removeObject(object);
                        logger.info("Player interacted with level door at coordinates: (" + levelDoorCenterX + ", " + levelDoorCenterY + ")");
                    }
                    return;
                }
            }
            if (object.getType().equals(GameObjectType.NPC)) {
                NPC npc = (NPC) object;
                int npcCenterX = Integer.parseInt(npc.getX()) + NPC_WIDTH / 2;
                int npcCenterY = Integer.parseInt(npc.getY()) + NPC_HEIGHT / 2;

                double distance = Math.sqrt(Math.pow(playerCenterX - npcCenterX, 2) + Math.pow(playerCenterY - npcCenterY, 2));

                if (distance <= INTERACTION_RADIUS + (radiusOfCollision + 5)) {
                    npc.interact(player);
                    logger.info("Player interacted with NPC at coordinates: (" + npcCenterX + ", " + npcCenterY + ")");
                    if (inventory.isQuestFinished() && !inventory.isFull()) {
                        player.getInventory().removeCoinFromBalance(3);
                        player.getInventory().addItem(new QuestKey(0, 0));
                        logger.info("Player finished quest and received Quest Key.");
                        npc.setTask1Complete(true);
                    }
                    gamePanel.repaint();
                    return;
                }
            }

            int objectCenterX = Integer.parseInt(object.getX()) + OBJECT_HEIGHT / 2;
            int objectCenterY = Integer.parseInt(object.getY()) + OBJECT_WIDTH / 2;

            double distance = Math.sqrt(Math.pow(playerCenterX - objectCenterX, 2) + Math.pow(playerCenterY - objectCenterY, 2));

            if (distance <= INTERACTION_RADIUS) {
                object.interact(player);
                logger.info("Player interacted with object of type: " + object.getType() + " at coordinates: (" + objectCenterX + ", " + objectCenterY + ")");
                if (object.getType().equals(GameObjectType.CHEST)) {
                    Chest chest = (Chest) object;
                    chest.interact(player);
                    player.setAnimationType(Player.AnimationType.OPEN);
                    if (chest.isOpened()) {
                        //WIN GAME
                        gameManager.showWinMenu();
                        gamePanel.repaint();
                    }
                }
                if (object.getType().equals(GameObjectType.KEY)) {
                    Key key = (Key) object;
                    if (inventory.isFull()) {
                        return;
                    }
                    key.interact(player);
                    gamePanel.repaint();
                    gamePanel.removeObject(object);
                }
                if (object.getType().equals(GameObjectType.COIN)) {
                    Coin coin = (Coin) object;
                    coin.interact(player);
                    logger.info("Player score increased");
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                }
                if (object.getType().equals(GameObjectType.POTION)) {
                    Potion potion = (Potion) object;
                    if (inventory.isFull()) {
                        logger.info("Inventory is full");
                        return;
                    }
                    potion.interact(player);
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                }
                if (object.getType().equals(GameObjectType.HEAL)) {
                    Heal heal = (Heal) object;
                    if (inventory.isFull()) {
                        logger.info("Inventory is full");
                        return;
                    }
                    heal.interact(player);
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                }
                if (object.getType().equals(GameObjectType.QUEST_KEY)) {
                    QuestKey questKey = (QuestKey) object;
                    if (inventory.isFull()) {
                        logger.info("Inventory is full");
                        return;
                    }
                    questKey.interact(player);
                    gamePanel.removeObject(object);
                    gamePanel.repaint();
                } else {
                    player.setAnimationType(Player.AnimationType.INTERACT);
                }
                gamePanel.repaint();
                break;
            }
        }
    }

    /**
     * Crafts a heal item by combining two potions in the inventory.
     */
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
            logger.info("Crafted Heal item from two Potions.");
        }
        gamePanel.repaint();
    }

    /**
     * Shows the inventory menu.
     */
    protected void showInventory() {
        gameManager.showInventoryMenu();
        logger.info("Inventory menu shown.");
    }

    /**
     * Checks if the inventory menu is currently being shown.
     *
     * @return true if the inventory menu is shown, false otherwise
     */
    protected boolean showingInventory() {
        return gameManager.showingInventory();
    }

    /**
     * Hides the inventory menu.
     */
    protected void hideInventory() {
        gameManager.hideInventoryMenu();
        logger.info("Inventory menu hidden.");
    }
}