package Core.View;

import Core.Controller.Controller;
import Core.Model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static Core.Util.Constants.SCREEN_SIZE_DIMENSION;
import static Core.Util.Constants.TILE_SIZE;

public class GamePanel extends JPanel {
    private Player player;
    private Level level;
    private List<Enemy> enemies = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();
    private JsonArray objectsArray;
    private AnimationManager animationManager = new AnimationManager();
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private final int frameWidth = 16;
    private final int frameHeight = 16;
    private BufferedImage heartImage;

    private double zoomFactor = 2.0;

    private int offsetX;
    private int offsetY;

    public GamePanel(Player player, Level level) {
        this.player = player;
        this.level = level;
        setMinimumSize(SCREEN_SIZE_DIMENSION);
        setPreferredSize(SCREEN_SIZE_DIMENSION);
        setMaximumSize(SCREEN_SIZE_DIMENSION);
        loadLevelObjects(level.getLevelJson());

        loadHeartImage(); // Load heart image
    }

    private void loadHeartImage() {
        heartImage = loadImage("/heart.png");
    }

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

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        repaint();
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

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

    private void loadLevelObjects(JsonObject levelJson) {
        // Загрузить массив объектов из JSON
        objectsArray = levelJson.getAsJsonArray("objects");

        // Загрузить объекты
        loadObjects(objectsArray, objects);
    }

    public static void loadObjects(JsonArray objectsArray, List<GameObject> objects) {
        for (JsonElement objElement : objectsArray) {
            JsonObject objJson = objElement.getAsJsonObject();
            GameObjectType type = GameObjectType.valueOf(objJson.get("type").getAsString().toUpperCase());
            int x = objJson.get("x").getAsInt();
            int y = objJson.get("y").getAsInt();
            // Создать объекты на основе типа
            GameObject gameObject = switch (type) {
                case CHEST -> new Chest(x, y);
                case KEY -> new Key(x, y);
                case COIN -> new Coin(x, y);
                case DOOR -> new Door(x, y);
                case NPC -> new NPC(x, y);
                case POTION -> new Potion(x, y);
                case HEAL -> new Heal(x, y);
                case QUEST_KEY -> new QuestKey(x, y);
                default -> null;
            };
            if (gameObject != null) {
                objects.add(gameObject);
            }
        }
    }

    public int getAbsoluteX(int x) {
        return x - offsetX;
    }

    public int getAbsoluteY(int y) {
        return y - offsetY;
    }



    @Override
    protected void paintComponent(Graphics g) {
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

            if (Objects.equals(GameObjectType, "CHEST")) {
                Chest chest = (Chest) object;
                if (chest.isOpened()) {
                    chest.drawOpened(g, objectX, objectY);
                } else {
                    chest.draw(g, objectX, objectY);
                }
            } else if (Objects.equals(GameObjectType, "KEY")) {
                Key key = (Key) object;
                if (key.isTaken()) {
                    key.drawTaken(g, objectX, objectY);
                } else {
                    key.draw(g, objectX, objectY);
                }
            } else if (Objects.equals(GameObjectType, "COIN")) {
                Coin coin = (Coin) object;
                if (coin.isCollected()) {
                    coin.drawCollected(g, objectX, objectY);
                } else {
                    coin.draw(g, objectX, objectY);
                }
            } else if (Objects.equals(GameObjectType, "DOOR")) {
                Door door = (Door) object;
                if (door.isOpened()) {
                    door.drawOpened(g, objectX, objectY);
                } else {
                    door.draw(g, objectX, objectY);
                }
            } else if (Objects.equals(GameObjectType, "NPC")) {
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
            }   else if (Objects.equals(GameObjectType, "POTION")) {
                Potion potion = (Potion) object;
                potion.draw(g, objectX, objectY);
            } else if (Objects.equals(GameObjectType, "HEAL")) {
                Heal heal = (Heal) object;
                heal.draw(g, objectX, objectY); }
            else {
                object.draw(g, objectX, objectY);
            }
        }

        int heartWidth = 16;
        int heartHeight = 16;
        int spacing = 2;

        int playerHealth = player.getHealth();
        int heartCount = (int) Math.ceil((double) playerHealth);

        for (int i = 0; i < heartCount; i++) {
            int heartY = 2;
            int heartX = 2;
            int drawX = heartX + spacing + i * (heartWidth + spacing);
            int drawY = heartY + spacing;
            g.drawImage(heartImage, drawX, drawY, heartWidth, heartHeight, null);
        }
        g.translate(-offsetX, -offsetY);
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    private void drawTile(Graphics g, Tile tile, int x, int y) {
        BufferedImage image = getImageForSurfaceType(tile.getSurfaceType());
        if (image != null) {
            int newTileWidth = TILE_SIZE;
            int newTileHeight = TILE_SIZE;
            g.drawImage(image, x * newTileWidth, y * newTileHeight, newTileWidth, newTileHeight, null);
        }
    }

    private BufferedImage getImageForSurfaceType(SurfaceType surfaceType) {
        String imagePath = surfaceType.getTexturePath();
        return getImageFromCache(imagePath);
    }

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

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }


    public GameObject[] getObjects() {
        return objects.toArray(new GameObject[0]);
    }

    public void removeObject(GameObject object) {
        objects.remove(object);
    }
}
