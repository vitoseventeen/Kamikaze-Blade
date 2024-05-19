package Core.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Enumeration representing different types of surfaces in the game.
 */
public enum SurfaceType {
    WALL("/Wall.png"),
    FLOOR("/Floor.png"),
    EMPTY("/Empty.png"),
    LEVELTILE("/LevelTile.png");

    private final String texturePath;
    private final BufferedImage textureImage;

    /**
     * Constructs a SurfaceType with the specified texture path.
     *
     * @param texturePath the path to the texture image
     */
    SurfaceType(String texturePath) {
        this.texturePath = texturePath;
        this.textureImage = loadImage(texturePath);
    }

    /**
     * Retrieves the texture path of the surface type.
     *
     * @return the texture path
     */
    public String getTexturePath() {
        return texturePath;
    }

    /**
     * Retrieves the texture image of the surface type.
     *
     * @return the texture image
     */
    public BufferedImage getTextureImage() {
        return textureImage;
    }

    /**
     * Loads an image from the specified path.
     *
     * @param texturePath the path to the image
     * @return the loaded image
     */
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

    /**
     * Retrieves the texture image of the surface type.
     *
     * @return the texture image
     */
    public BufferedImage getImage() {
        return textureImage;
    }
}
