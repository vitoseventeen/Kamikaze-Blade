package Core.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public enum SurfaceType {
    WALL("/Wall.png"),
    FLOOR("/Floor.png"),
    EMPTY("/Empty.png");

    private final String texturePath;
    private BufferedImage textureImage;

    SurfaceType(String texturePath) {
        this.texturePath = texturePath;
        this.textureImage = loadImage(texturePath);
    }

    public String getTexturePath() {
        return texturePath;
    }

    public BufferedImage getTextureImage() {
        return textureImage;
    }

    private BufferedImage loadImage(String texturePath) {
        try (InputStream is = getClass().getResourceAsStream(texturePath)) {
            if (is != null) {
                return ImageIO.read(is);
            } else {
                System.err.println("Unable to load texture image: " + texturePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage getImage() {
        return textureImage;
    }
}
