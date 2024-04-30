package Core.View;

import Core.Model.Player;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class AnimationManager {
    private Map<String, BufferedImage[][]> animations = new HashMap<>();
    private Map<String, Integer> animationTicks = new HashMap<>();
    private Map<String, Integer> animationIndices = new HashMap<>();
    private int animationSpeed = 3;

    public void addAnimations(String name, String imagePath, int frameWidth, int frameHeight, int animationCount, int directionCount) {
        BufferedImage[][] frames = new BufferedImage[animationCount][directionCount];
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                BufferedImage image = ImageIO.read(is);
                for (int i = 0; i < animationCount; i++) {
                    for (int j = 0; j < directionCount; j++) {
                        frames[i][j] = image.getSubimage(j * frameWidth, i * frameHeight, frameWidth, frameHeight);
                    }
                }
                animations.put(name, frames);
                animationTicks.put(name, 0);
                animationIndices.put(name, 0);
            } else {
                System.err.println("Unable to load image: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getFrame(String name, Player.Direction direction, Player.AnimationType animationType) {
        int row = animationType.ordinal();
        int col = direction.ordinal();

        BufferedImage[][] frames = animations.get(name);
        if (frames != null && row < frames.length && col < frames[row].length) {
            return frames[row][col];
        } else {
            return null;
        }
    }
}
