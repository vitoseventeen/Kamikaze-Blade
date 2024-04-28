package Core.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AnimationManager {
    private Map<String, BufferedImage[]> animations = new HashMap<>();
    private Map<String, Integer> animationTicks = new HashMap<>();
    private Map<String, Integer> animationIndices = new HashMap<>();
    private int animationSpeed = 3;

    public void addAnimation(String name, String imagePath, int frameWidth, int frameHeight, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                BufferedImage image = ImageIO.read(is);
                for (int i = 0; i < frameCount; i++) {
                    frames[i] = image.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
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

    public BufferedImage getFrame(String name) {
        BufferedImage[] frames = animations.get(name);
        if (frames != null) {
            int tick = animationTicks.getOrDefault(name, 0);
            int index = animationIndices.getOrDefault(name, 0);
            tick++;
            if (tick >= animationSpeed) {
                tick = 0;
                index++;
                if (index >= frames.length) {
                    index = 0;
                }
            }
            animationTicks.put(name, tick);
            animationIndices.put(name, index);
            return frames[index];
        } else {
            return null;
        }
    }
}

