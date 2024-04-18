package Core.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Level {
    private BufferedImage levelImage;

    public Level(String imagePath) {
        importLevelImage(imagePath);
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

    public BufferedImage getLevelImage() {
        return levelImage;
    }
}
