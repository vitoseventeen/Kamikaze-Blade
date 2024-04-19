package Core.Model;

import Core.Util.Constants;

import java.util.Random;

public class LevelFactory {
    public static Level createLevel() {
        Random random = new Random();
        int tilesWide = 1280 / Constants.TILE_SIZE;
        int tilesHigh = 800 / Constants.TILE_SIZE;

        Tile[][] tiles = new Tile[tilesWide][tilesHigh];
        for (int x = 0; x < tilesWide; x++) {
            for (int y = 0; y < tilesHigh; y++) {
                SurfaceType surfaceType = random.nextBoolean() ? SurfaceType.GRASS : SurfaceType.WALL;
                tiles[x][y] = new Tile(surfaceType);
            }
        }
        return new Level(tiles);
    }
}
