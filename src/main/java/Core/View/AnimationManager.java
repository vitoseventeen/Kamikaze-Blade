    package Core.View;

    import Core.Model.Enemy;
    import Core.Model.Player;

    import javax.imageio.ImageIO;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.HashMap;
    import java.util.Map;

    public class AnimationManager {
        private Map<String, BufferedImage[][]> animations = new HashMap<>();
        private Map<String, Integer> animationTicks = new HashMap<>();
        private Map<String, Integer> animationIndices = new HashMap<>();
        private Map<String, Integer> animationSpeeds = new HashMap<>();

        public AnimationManager() {
            addAnimations("idle", "/Idle.png", 16, 16, 1, 4);
            addAnimations("walk", "/Walk.png", 16, 16, 4, 4);
            addAnimations("attack", "/Attack.png", 16, 16, 1, 4);
            addAnimations("death", "/Dead.png", 16, 16, 1, 1);
            addAnimations("interact", "/Item.png", 16, 16, 1, 1);
            addAnimations("open","/attack.png",16,16,1,4);

            setAnimationSpeed("idle", 20);
            setAnimationSpeed("open", 20);
            setAnimationSpeed("walk", 20);
            setAnimationSpeed("enemyWalk",30);
            setAnimationSpeed("attack", 20);
            setAnimationSpeed("enemyAttack",30);
            setAnimationSpeed("death", 20);
            setAnimationSpeed("enemyDeath",30);
            setAnimationSpeed("interact", 20);

            addAnimations("enemyIdle", "/enemyIdle.png", 16, 16, 1, 4);
            addAnimations("enemyWalk", "/enemyWalk.png", 16, 16, 4, 4);
            addAnimations("enemyAttack", "/enemyAttack.png", 16, 16, 1,4);
            addAnimations("enemyDeath", "/enemyDead.png", 16, 16, 1, 1);
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
            int row;
            if (animationType == Player.AnimationType.ATTACK || animationType == Player.AnimationType.IDLE || animationType == Player.AnimationType.DEATH || animationType == Player.AnimationType.INTERACT || animationType == Player.AnimationType.OPEN) {
                row = 0;
            } else {
                row = animationType.ordinal();
            }
            return getBufferedImage(name, direction, row);
        }
        public BufferedImage getEnemyFrame(String name, Enemy.Direction direction, Enemy.AnimationType animationType) {
            int row;
            if (animationType == Enemy.AnimationType.ATTACK || animationType == Enemy.AnimationType.IDLE || animationType == Enemy.AnimationType.DEATH ) {
                row = 0;
            } else {
                row = animationType.ordinal();
            }
            return getBufferedImage(name, direction, row);
        }

        private BufferedImage getBufferedImage(String name, Player.Direction direction, int row) {
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
