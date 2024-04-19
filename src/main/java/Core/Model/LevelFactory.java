package Core.Model;

import Core.Util.Constants;

import java.util.Random;

import static Core.Util.Constants.SCREEN_HEIGHT;
import static Core.Util.Constants.SCREEN_WIDTH;

public class LevelFactory {
    public static Level createLevel() {
        Random random = new Random();
        int tilesWide = SCREEN_WIDTH / Constants.TILE_SIZE;
        int tilesHigh = SCREEN_HEIGHT / Constants.TILE_SIZE;

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
