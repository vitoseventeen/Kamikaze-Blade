package Core.Model;

import static Core.Util.Constants.*;

public class LevelFactory {
    public static Level createLevel() {
        int tilesWide = SCREEN_WIDTH / TILE_SIZE;
        int tilesHigh = SCREEN_HEIGHT / TILE_SIZE;


        if (SCREEN_WIDTH % TILE_SIZE != 0) {
            tilesWide++;
        }
        if (SCREEN_HEIGHT % TILE_SIZE != 0) {
            tilesHigh++;
        }

        Tile[][] tiles = new Tile[tilesWide][tilesHigh];
        for (int x = 0; x < tilesWide; x++) {
            for (int y = 0; y < tilesHigh; y++) {
                SurfaceType surfaceType = SurfaceType.GRASS;
                tiles[x][y] = new Tile(surfaceType);
            }
        }
        return new Level(tiles);
    }
}
