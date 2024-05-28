package View;

import Model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static Util.Constants.SCREEN_SIZE_DIMENSION;
import static Util.Constants.TILE_SIZE;

/**
 * A JPanel subclass for displaying the game graphics.
 */
public class GamePanel extends JPanel {
    private final Player player;
    private Level level;
    private List<Enemy> enemies = new ArrayList<>();
    private static final List<GameObject> objects = new ArrayList<>();
    private final AnimationManager animationManager = new AnimationManager();
    private final Map<String, BufferedImage> imageCache = new HashMap<>();
    private final int frameWidth = 16;
    private final int frameHeight = 16;
    private BufferedImage heartImage;

    private double zoomFactor = 2.0;

    private int offsetX;
    private int offsetY;

    /**
     * Constructs a GamePanel with the specified player and level.
     *
     * @param player The player object.
     * @param level  The level object.
     */
    public GamePanel(Player player, Level level) {
        this.player = player;
        this.level = level;
        setMinimumSize(SCREEN_SIZE_DIMENSION);
        setPreferredSize(SCREEN_SIZE_DIMENSION);
        setMaximumSize(SCREEN_SIZE_DIMENSION);
        loadLevelObjects(level.getLevelJson());
        loadHeartImage();
    }

    /**
     * Loads the heart image.
     */
    private void loadHeartImage() {
        heartImage = loadImage("/heart.png");
    }

    /**
     * Loads an image from the given path.
     *
     * @param path The path of the image file.
     * @return The loaded image.
     */
    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            } else {
                System.err.println("Unable to load image: " + path);
                System.err.println("You have to make 'assets' folder as a source root in your IDE.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the zoom factor of the panel.
     *
     * @param zoomFactor The zoom factor to set.
     */
    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        repaint();
    }

    /**
     * Gets the current zoom factor of the panel.
     *
     * @return The current zoom factor.
     */
    public double getZoomFactor() {
        return zoomFactor;
    }

    /**
     * Centers the camera on the player.
     *
     * @param g2d The Graphics2D object.
     */
    private void centerCameraOnPlayer(Graphics2D g2d) {
        int playerX = player.getX();
        int playerY = player.getY();

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int halfPanelWidth = (int) (panelWidth / (2 * zoomFactor));
        int halfPanelHeight = (int) (panelHeight / (2 * zoomFactor));

        offsetX = halfPanelWidth - playerX - (frameWidth / 2);
        offsetY = halfPanelHeight - playerY - (frameHeight / 2);

        offsetX = Math.min(offsetX, 0);
        offsetY = Math.min(offsetY, 0);

        offsetX = Math.max(offsetX, halfPanelWidth * 2 - level.getWidth() * TILE_SIZE);
        offsetY = Math.max(offsetY, halfPanelHeight * 2 - level.getHeight() * TILE_SIZE);

        g2d.translate(offsetX, offsetY);
    }

    /**
     * Loads objects from the level JSON.
     *
     * @param levelJson The JSON object representing the level.
     */
    private void loadLevelObjects(JsonObject levelJson) {
        JsonArray objectsArray = levelJson.getAsJsonArray("objects");
        loadObjects(objectsArray, objects);
    }

    /**
     * Loads objects from a JSON array.
     *
     * @param objectsArray The JSON array of objects.
     * @param objects      The list to which objects will be added.
     */
    public static void loadObjects(JsonArray objectsArray, List<GameObject> objects) {
        for (JsonElement objElement : objectsArray) {
            JsonObject objJson = objElement.getAsJsonObject();
            GameObjectType type = GameObjectType.valueOf(objJson.get("type").getAsString().toUpperCase());
            int x = objJson.get("x").getAsInt();
            int y = objJson.get("y").getAsInt();
            GameObject gameObject = switch (type) {
                case CHEST -> new Chest(x, y);
                case KEY -> new Key(x, y);
                case COIN -> new Coin(x, y);
                case DOOR -> new Door(x, y);
                case NPC -> new NPC(x, y);
                case POTION -> new Potion(x, y);
                case HEAL -> new Heal(x, y);
                case QUEST_KEY -> new QuestKey(x, y);
                case LEVELDOOR -> new LevelDoor(x, y);
                default -> null;
            };
            if (gameObject != null) {
                objects.add(gameObject);
            }
        }
    }

    /**
     * Paints the game graphics.
     *
     * @param g The Graphics object.
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactor, zoomFactor);

        centerCameraOnPlayer(g2d);

        if (level != null) {
            for (int x = 0; x < level.getWidth(); x++) {
                for (int y = 0; y < level.getHeight(); y++) {
                    Tile tile = level.getTile(x, y);
                    drawTile(g, tile, x, y);
                }
            }
        }

        BufferedImage playerFrame = switch (player.getAnimationType()) {
            case WALK -> {
                animationManager.updateAnimation("walk");
                yield animationManager.getFrame("walk", player.getDirection(), player.getAnimationType());
            }
            case ATTACK -> {
                animationManager.updateAnimation("attack");
                yield animationManager.getFrame("attack", player.getDirection(), player.getAnimationType());
            }
            case DEATH -> {
                animationManager.updateAnimation("death");
                yield animationManager.getFrame("death", Player.Direction.UP, Player.AnimationType.DEATH);
            }
            default -> {
                animationManager.updateAnimation("idle");
                yield animationManager.getFrame("idle", Player.Direction.UP, Player.AnimationType.IDLE);
            }
        };

        if (playerFrame != null) {
            int playerX = player.getX();
            int playerY = player.getY();
            playerX = Math.max(0, Math.min(playerX, getWidth() - frameWidth));
            playerY = Math.max(0, Math.min(playerY, getHeight() - frameHeight));
            g.drawImage(playerFrame, playerX, playerY, null);
        }

        // Draw hearts
        g.translate(-offsetX, -offsetY);

        // Draw enemies
        for (Enemy enemy : enemies) {
            BufferedImage enemyFrame = switch (enemy.getAnimationType()) {
                case WALK -> {
                    animationManager.updateAnimation("enemyWalk");
                    yield animationManager.getEnemyFrame("enemyWalk", enemy.getDirection(), enemy.getAnimationType());
                }
                case ATTACK -> {
                    animationManager.updateAnimation("enemyAttack");
                    yield animationManager.getEnemyFrame("enemyAttack", enemy.getDirection(), enemy.getAnimationType());
                }
                case DEATH -> {
                    animationManager.updateAnimation("enemyDeath");
                    yield animationManager.getEnemyFrame("enemyDeath", Player.Direction.UP, Player.AnimationType.DEATH);
                }
                default -> {
                    animationManager.updateAnimation("enemyIdle");
                    yield animationManager.getEnemyFrame("enemyIdle", enemy.getDirection(), enemy.getAnimationType());
                }
            };
            if (enemyFrame != null) {
                int enemyX = enemy.getX() + offsetX;
                int enemyY = enemy.getY() + offsetY;

                g.drawImage(enemyFrame, enemyX, enemyY, null);
            }
        }

        // draw level objects
        for (GameObject object : objects) {
            int objectX = Integer.parseInt(object.getX()) + offsetX;
            int objectY = Integer.parseInt(object.getY()) + offsetY;
            String GameObjectType = String.valueOf(object.getType());

            switch (GameObjectType) {
                case "CHEST" -> {
                    Chest chest = (Chest) object;
                    if (chest.isOpened()) {
                        chest.drawOpened(g, objectX, objectY);
                    } else {
                        chest.draw(g, objectX, objectY);
                    }
                }
                case "KEY" -> {
                    Key key = (Key) object;
                    if (key.isTaken()) {
                        key.drawTaken(g, objectX, objectY);
                    } else {
                        key.draw(g, objectX, objectY);
                    }
                }
                case "COIN" -> {
                    Coin coin = (Coin) object;
                    if (coin.isCollected()) {
                        coin.drawCollected(g, objectX, objectY);
                    } else {
                        coin.draw(g, objectX, objectY);
                    }
                }
                case "DOOR" -> {
                    Door door = (Door) object;
                    if (door.isOpened()) {
                        door.drawOpened(g, objectX, objectY);
                    } else {
                        door.draw(g, objectX, objectY);
                    }
                }
                case "LEVELDOOR" -> {
                    LevelDoor levelDoor = (LevelDoor) object;
                    if (levelDoor.isOpened()) {
                        levelDoor.drawOpened(g, objectX, objectY);
                    } else {
                        levelDoor.draw(g, objectX, objectY);
                    }
                }
                case "NPC" -> {
                    NPC npc = (NPC) object;
                    npc.draw(g, objectX, objectY);
                    if (npc.isTalking()) {
                        g.drawImage(npc.getTask1(), objectX + 20, objectY - 30, npc.getTask1().getWidth(null) / 7, npc.getTask1().getHeight(null) / 7, null);
                        if (npc.isTask1Complete()) {
                            g.clearRect(objectX + 20, objectY - 30, npc.getTask1().getWidth(null) / 7, npc.getTask1().getHeight(null) / 7);
                            npc.drawAfterQuest(g, objectX, objectY);
                            g.drawImage(npc.getTaskCompleted(), objectX + 20, objectY - 30, npc.getTaskCompleted().getWidth(null) / 7, npc.getTaskCompleted().getHeight(null) / 7, null);
                        }
                    }
                }
                case "POTION" -> {
                    Potion potion = (Potion) object;
                    if (potion.isTaken()) {
                        potion.drawTaken(g, objectX, objectY);
                    } else {
                        potion.draw(g, objectX, objectY);
                    }
                }
                case "HEAL" -> {
                    Heal heal = (Heal) object;
                    if (heal.isTaken()) {
                        heal.drawTaken(g, objectX, objectY);
                    } else {
                        heal.draw(g, objectX, objectY);
                    }
                }
                case null, default -> object.draw(g, objectX, objectY);
            }
        }

        int heartWidth = 16;
        int heartHeight = 16;
        int spacing = 2;

        int playerHealth = player.getHealth();
        int heartCount = (int) (double) playerHealth;

        for (int i = 0; i < heartCount; i++) {
            int heartY = 2;
            int heartX = 2;
            int drawX = heartX + spacing + i * (heartWidth + spacing);
            int drawY = heartY + spacing;
            g.drawImage(heartImage, drawX, drawY, heartWidth, heartHeight, null);
        }
        g.translate(-offsetX, -offsetY);
    }

    /**
     * Draws a tile at the specified position.
     *
     * @param g   The Graphics object.
     * @param tile The tile to draw.
     * @param x    The x-coordinate of the tile.
     * @param y    The y-coordinate of the tile.
     */

    public void drawTile(Graphics g, Tile tile, int x, int y) {
        BufferedImage image = getImageForSurfaceType(tile.getSurfaceType());
        if (image != null) {
            int newTileWidth = TILE_SIZE;
            int newTileHeight = TILE_SIZE;
            g.drawImage(image, x * newTileWidth, y * newTileHeight, newTileWidth, newTileHeight, null);
        }
    }

    /**
     * Retrieves an image for a surface type.
     *
     * @param surfaceType The surface type.
     * @return The image for the surface type.
     */

    private BufferedImage getImageForSurfaceType(SurfaceType surfaceType) {
        String imagePath = surfaceType.getTexturePath();
        return getImageFromCache(imagePath);
    }
    /**
     * Retrieves an image from the cache.
     *
     * @param path The path of the image.
     * @return The image from the cache.
     */

    private BufferedImage getImageFromCache(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        } else {
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    BufferedImage image = ImageIO.read(is);
                    imageCache.put(path, image);
                    return image;
                } else {
                    System.err.println("Unable to load image: " + path);
                    System.err.println("You have to make 'assets' folder as a source root in your IDE.");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Sets the enemies in the level.
     *
     * @param enemies The list of enemies.
     */

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     * Retrieves the objects in the level.
     *
     * @return The objects in the level.
     */

    public GameObject[] getObjects() {
        return objects.toArray(new GameObject[0]);
    }

    /**
     * Removes an object from the level.
     *
     * @param object The object to remove.
     */

    public void removeObject(GameObject object) {
        objects.remove(object);
    }

    /**
     * Sets the level of the game.
     *
     * @param level The level to set.
     */

    public void setLevel(Level level) {
        this.level = level;
    }


    /**
     * Adds an object to the level.
     *
     * @param object The object to add.
     */

    public void addObject(GameObject object) {
        objects.add(object);
    }

    /**
     * Clears all objects from the level.
     */

    public void clearObjects() {
        objects.clear();
    }

}
