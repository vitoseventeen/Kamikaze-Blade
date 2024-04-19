package Core.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import Core.Model.Player;
import Core.Model.Level;
import Core.Model.LevelManager;
import Core.Model.Tile;
import Core.Model.SurfaceType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static Core.Util.Constants.SCREEN_SIZE;
import static Core.Util.Constants.TILE_SIZE;

public class Panel extends JPanel {
    private BufferedImage playerImage;
    private BufferedImage[] idleAnimations;
    private int animationTick;
    private int animationInd;
    private int animationSpeed = 3;
    private Player player;
    private LevelManager levelManager;
    private Level level;

    public Panel(Player player) {
        this.player = player;
        setMinimumSize(SCREEN_SIZE);
        setPreferredSize(SCREEN_SIZE);
        setMaximumSize(SCREEN_SIZE);
        levelManager = new LevelManager();
        level = levelManager.getCurrentLevel();
        importPlayerImage();
        animatePlayer();
    }


    private void animatePlayer() {
        if (playerImage != null) {
            idleAnimations = new BufferedImage[11];

            for (int i = 0; i < idleAnimations.length; i++) {
                idleAnimations[i] = playerImage.getSubimage(i * 32, 0, 32, 32);
            }
        } else {
            System.err.println("Player image is null, unable to animate player.");
        }
    }

    private void importPlayerImage() {
        String imagePath = "/Idle.png";
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                playerImage = ImageIO.read(is);
            } else {
                System.err.println("Unable to load player image");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // Отрисовка игрока
        if (playerImage != null) {
            g.drawImage(idleAnimations[animationInd], player.getX(), player.getY(), null);
        }

        updateAnimation();
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
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                return ImageIO.read(is);
            } else {
                System.err.println("Unable to load image for surface type: " + surfaceType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void updateAnimation() { // aktualizace animace
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationInd++;
            if (animationInd >= idleAnimations.length) {
                animationInd = 0;
            }
        }
    }
}
