package Core.View.Global;

import javax.imageio.ImageIO;
import javax.swing.*;
import Core.Model.characters.Player;
import Core.Model.level.BufferedImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Panel extends JPanel {


    private BufferedImage playerImage;
    private BufferedImage[] idleAnimations;
    private int animationTick;
    private int animationInd;
    private int animationSpeed = 3;
    private Player player;

    private BufferedImage levelImage;


    public Panel(Player player) {
        this.player = player;
        Dimension size = new Dimension(1280, 800);

        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        importPlayerImage();
        importLevelImage();
        animatePlayer();
    }

    private void importLevelImage() {
        String path = "/level.png";
        try { InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                levelImage = ImageIO.read(is);
            } else {
                System.err.println("Unable to load level image");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void animatePlayer() {
        idleAnimations = new BufferedImage[11];

        for (int i = 0; i < idleAnimations.length; i++) {
            idleAnimations[i] = playerImage.getSubimage(i * 32, 0, 32, 32);
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
        updateAnimation();

        if (levelImage != null) {
            g.drawImage(levelImage, 0, 0, getWidth(), getHeight(), null);
        }

        BufferedImage currentAnimation = idleAnimations[animationInd];
        g.drawImage(currentAnimation, player.getX(), player.getY(), 64, 64, null); // zvetseni modelu hraca
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

