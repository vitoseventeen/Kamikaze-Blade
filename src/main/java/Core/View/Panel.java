package Core.View;

import Core.Controller.Controller;
import Core.Model.Level;
import Core.Model.Player;
import Core.Model.Tile;
import Core.Model.SurfaceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static Core.Util.Constants.SCREEN_SIZE_DIMENSION;
import static Core.Util.Constants.TILE_SIZE;

public class Panel extends JPanel {
    private Player player;
    private Level level;
    private AnimationManager animationManager = new AnimationManager();
    private Map<String, BufferedImage> imageCache = new HashMap<>();

    public Panel(Player player, Level level) {
        this.player = player;
        this.level = level;
        setMinimumSize(SCREEN_SIZE_DIMENSION);
        setPreferredSize(SCREEN_SIZE_DIMENSION);
        setMaximumSize(SCREEN_SIZE_DIMENSION);
        initializeAnimationManager();
    }

    private void initializeAnimationManager() {
        animationManager.addAnimation("player_idle", "/Idle.png", 32, 32, 11);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (level != null) {
            for (int x = 0; x < level.getWidth(); x++) {
                for (int y = 0; y < level.getHeight(); y++) {
                    Tile tile = level.getTile(x, y);
                    drawTile(g, tile, x, y);
                }
            }
        }

        BufferedImage playerFrame = animationManager.getFrame("player_idle");
        if (playerFrame != null) {
            int playerX = player.getX();
            int playerY = player.getY();
            playerX = Math.max(0, Math.min(playerX, getWidth() - 32));
            playerY = Math.max(0, Math.min(playerY, getHeight() - 32));
            g.drawImage(playerFrame, playerX, playerY, null);
        }
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
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
