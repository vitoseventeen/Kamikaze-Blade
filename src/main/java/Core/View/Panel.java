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
    private final int frameWidth = 16;
    private final int frameHeight = 16;

    public Panel(Player player, Level level) {
        this.player = player;
        this.level = level;
        setMinimumSize(SCREEN_SIZE_DIMENSION);
        setPreferredSize(SCREEN_SIZE_DIMENSION);
        setMaximumSize(SCREEN_SIZE_DIMENSION);
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

        BufferedImage playerFrame;
        if (player.getAnimationType() == Player.AnimationType.WALK) {
            animationManager.updateAnimation("walk");
            playerFrame = animationManager.getFrame("walk", player.getDirection(), player.getAnimationType());
        } else if (player.getAnimationType() == Player.AnimationType.ATTACK) {
            animationManager.updateAnimation("attack");
            playerFrame = animationManager.getFrame("attack", player.getDirection(), player.getAnimationType());
        } else {
            playerFrame = animationManager.getFrame("idle", player.getDirection(), player.getAnimationType());
        }

        if (playerFrame != null) {
            int playerX = player.getX();
            int playerY = player.getY();
            playerX = Math.max(0, Math.min(playerX, getWidth() - frameWidth));
            playerY = Math.max(0, Math.min(playerY, getHeight() - frameHeight));
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