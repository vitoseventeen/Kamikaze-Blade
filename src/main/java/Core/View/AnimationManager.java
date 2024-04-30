package Core.View;

import Core.Model.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static Core.Util.Constants.PLAYER_HEIGHT;
import static Core.Util.Constants.PLAYER_WIDTH;

public class AnimationManager {
    private Map<String, BufferedImage[][]> animations = new HashMap<>();
    private Map<String, Integer> animationTicks = new HashMap<>();
    private Map<String, Integer> animationIndices = new HashMap<>();
    private Map<String, Integer> animationSpeeds = new HashMap<>();

    public AnimationManager() {
        addAnimations("idle", "/idle.png", PLAYER_WIDTH, PLAYER_HEIGHT, 1, 4);
        addAnimations("walk", "/walk.png", PLAYER_WIDTH, PLAYER_HEIGHT, 4, 4);
        addAnimations("attack", "/attack.png", PLAYER_WIDTH, PLAYER_HEIGHT, 1, 4);

        setAnimationSpeed("walk", 60);

    }

    private void addAnimations(String name, String imagePath, int frameWidth, int frameHeight, int animationCount, int directionCount) {
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
            int index = animationIndices.get(name);
            return frames[index][col];
        } else {
            return null;
        }
    }

    public void updateAnimation(String name) {
        int ticks = animationTicks.get(name);
        int speed = animationSpeeds.getOrDefault(name, 1);
        if (ticks % speed == 0) {
            int index = animationIndices.get(name);
            index = (index + 1) % animations.get(name).length;
            animationIndices.put(name, index);
        }
        animationTicks.put(name, ticks + 1);
    }

    public void setAnimationSpeed(String name, int speed) {
        animationSpeeds.put(name, speed);
    }
}


