package Core.Model;

import java.awt.image.BufferedImage;

public class Level {
    private final Tile[][] tiles;

    public Level(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            return tiles[x][y];
        } else {
            return new Tile(SurfaceType.EMPTY);
        }
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }
}
