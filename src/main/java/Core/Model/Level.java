package Core.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Level {
    private BufferedImage levelImage;
    private BufferedImage levelMask;

    public Level(String imagePath, String maskPath) {
        importLevelImage(imagePath);
        importLevelMask(maskPath);
    }


    private void importLevelImage(String imagePath) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                levelImage = ImageIO.read(is);
            } else {
                System.err.println("Unable to load level image");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void importLevelMask(String maskPath) {
        try (InputStream is = getClass().getResourceAsStream(maskPath)) {
            if (is != null) {
                levelMask = ImageIO.read(is);
            } else {
                System.err.println("Unable to load level mask");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getLevelImage() {
        return levelImage;
    }
    public BufferedImage getLevelMask() {
        return levelMask;
    }

}
