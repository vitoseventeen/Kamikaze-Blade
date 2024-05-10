package Core.Model;

import Core.Model.SurfaceType;
import Core.Model.Tile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private final Tile[][] tiles;
    private static List<Level> levels = new ArrayList<>();

    public Level(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public static Level loadLevelFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            JsonObject levelJson = gson.fromJson(reader, JsonObject.class);

            int width = levelJson.get("width").getAsInt();
            int height = levelJson.get("height").getAsInt();
            JsonArray tilesArray = levelJson.getAsJsonArray("tiles");

            Tile[][] tiles = new Tile[width][height];
            for (int y = 0; y < height; y++) {
                JsonArray row = tilesArray.get(y).getAsJsonArray();
                for (int x = 0; x < width; x++) {
                    int tileType = row.get(x).getAsInt();
                    SurfaceType surfaceType = switch (tileType) {
                        case 0 -> SurfaceType.GRASS;
                        case 1 -> SurfaceType.WALL;
                        case 2 -> SurfaceType.DOOR;
                        default -> SurfaceType.EMPTY;
                    };
                    tiles[x][y] = new Tile(surfaceType);
                }
            }

            Level level = new Level(tiles);
            levels.add(level);
            return level;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
