package Core.View;

import Core.Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Core.Util.Constants.SCREEN_SIZE_DIMENSION;
import static Core.Util.Constants.TILE_SIZE;

public class Panel extends JPanel {
    private Player player;
    private Level level;
    private List<Enemy> enemies = new ArrayList<>();
    private AnimationManager animationManager = new AnimationManager();
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private final int frameWidth = 16;
    private final int frameHeight = 16;
    private BufferedImage heartImage;

    private double zoomFactor = 2.0;

    private int offsetX;
    private int offsetY;

    public Panel(Player player, Level level) {
        this.player = player;
        this.level = level;
        setMinimumSize(SCREEN_SIZE_DIMENSION);
        setPreferredSize(SCREEN_SIZE_DIMENSION);
        setMaximumSize(SCREEN_SIZE_DIMENSION);

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
        int halfWidth = (int) (getWidth() / (2 * zoomFactor));
        int halfHeight = (int) (getHeight() / (2 * zoomFactor));

        offsetX = halfWidth - player.getX() - (frameWidth / 2);
        offsetY = halfHeight - player.getY() - (frameHeight / 2);

        offsetX = Math.min(offsetX, 0);
        offsetY = Math.min(offsetY, 0);

        offsetX = Math.max(offsetX, halfWidth * 2 - level.getWidth() * TILE_SIZE);
        offsetY = Math.max(offsetY, halfHeight * 2 - level.getHeight() * TILE_SIZE);

        g2d.translate(offsetX, offsetY);
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

        BufferedImage playerFrame;
        switch (player.getAnimationType()) {
            case WALK:
                animationManager.updateAnimation("walk");
                playerFrame = animationManager.getFrame("walk", player.getDirection(), player.getAnimationType());
                break;
            case ATTACK:
                animationManager.updateAnimation("attack");
                playerFrame = animationManager.getFrame("attack", player.getDirection(), player.getAnimationType());
                break;
            case DEATH:
                animationManager.updateAnimation("death");
                playerFrame = animationManager.getFrame("death", Player.Direction.UP, Player.AnimationType.DEATH);
                break;
            default:
                animationManager.updateAnimation("idle");
                playerFrame = animationManager.getFrame("idle", Player.Direction.UP, Player.AnimationType.IDLE);
                break;
        }

        if (playerFrame != null) {
            int playerX = player.getX();
            int playerY = player.getY();
            playerX = Math.max(0, Math.min(playerX, getWidth() - frameWidth));
            playerY = Math.max(0, Math.min(playerY, getHeight() - frameHeight));
            g.drawImage(playerFrame, playerX, playerY, null);
        }

        // Draw hearts
        g.translate(-offsetX, -offsetY);

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

        // Draw enemies
        for (Enemy enemy : enemies) {
            BufferedImage enemyFrame;
            switch (enemy.getAnimationType()) {
                case WALK:
                    animationManager.updateAnimation("enemyWalk");
                    enemyFrame = animationManager.getEnemyFrame("enemyWalk", enemy.getDirection(), enemy.getAnimationType());
                    break;
                case ATTACK:
                    animationManager.updateAnimation("enemyAttack");
                    enemyFrame = animationManager.getEnemyFrame("enemyAttack", enemy.getDirection(), enemy.getAnimationType());
                    break;
                case DEATH:
                    animationManager.updateAnimation("enemyDeath");
                    enemyFrame = animationManager.getEnemyFrame("enemyDeath", Player.Direction.UP, Player.AnimationType.DEATH);
                    break;
                default:
                    animationManager.updateAnimation("enemyIdle");
                    enemyFrame = animationManager.getEnemyFrame("enemyIdle", enemy.getDirection(), enemy.getAnimationType());
                    break;
            }

            if (enemyFrame != null) {
                int enemyX = enemy.getX() + offsetX;
                int enemyY = enemy.getY() + offsetY;
                g.drawImage(enemyFrame, enemyX, enemyY, null);
            }
        }


        g.translate(-offsetX, -offsetY);

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



}
