package Model;


import View.GamePanel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a level in the game.
 */
public class Level {
    /** List containing all levels created. */
    public static final List<Level> levels = new ArrayList<>();
    private final Tile[][] tiles;
    private final List<GameObject> objects;

    /**
     * Constructs a level with the given tiles and game objects.
     *
     * @param tiles   the tiles of the level
     * @param objects the game objects in the level
     */
    public Level(Tile[][] tiles, List<GameObject> objects) {
        this.tiles = tiles;
        this.objects = objects;
    }

    /**
     * Loads a level from a JSON file.
     *
     * @param filePath the path to the JSON file containing the level data
     * @return the loaded level, or null if loading fails
     */
    public static Level loadLevelFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            JsonObject levelJson = gson.fromJson(reader, JsonObject.class);

            int width = levelJson.get("width").getAsInt();
            int height = levelJson.get("height").getAsInt();
            JsonArray tilesArray = levelJson.getAsJsonArray("tiles");
            JsonArray objectsArray = levelJson.getAsJsonArray("objects");

            Tile[][] tiles = new Tile[width][height];
            List<GameObject> objects = new ArrayList<>();

            for (int y = 0; y < height; y++) {
                JsonArray row = tilesArray.get(y).getAsJsonArray();
                for (int x = 0; x < width; x++) {
                    int tileType = row.get(x).getAsInt();
                    SurfaceType surfaceType = switch (tileType) {
                        case 0 -> SurfaceType.FLOOR;
                        case 1 -> SurfaceType.WALL;
                        case 2 -> SurfaceType.LEVELTILE;
                        default -> SurfaceType.EMPTY;
                    };
                    tiles[x][y] = new Tile(surfaceType);
                }
            }

            GamePanel.loadObjects(objectsArray, objects);

            Level level = new Level(tiles, objects);
            levels.add(level);
            return level;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the game objects in the level.
     *
     * @return the list of game objects
     */
    public List<GameObject> getObjects() {
        return objects;
    }

    /**
     * Retrieves the tile at the specified position.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the tile at the specified position, or an empty tile if out of bounds
     */
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            return tiles[x][y];
        } else {
            return new Tile(SurfaceType.EMPTY);
        }
    }

    /**
     * Retrieves the width of the level.
     *
     * @return the width of the level in tiles
     */
    public int getWidth() {
        return tiles.length;
    }

    /**
     * Generates a JSON representation of the level.
     *
     * @return the JSON representation of the level
     */
    public JsonObject getLevelJson() {
        JsonObject levelJson = new JsonObject();
        levelJson.addProperty("width", getWidth());
        levelJson.addProperty("height", getHeight());

        JsonArray tilesArray = new JsonArray();
        for (int y = 0; y < getHeight(); y++) {
            JsonArray row = new JsonArray();
            for (int x = 0; x < getWidth(); x++) {
                row.add(getTile(x, y).getSurfaceType().ordinal());
            }
            tilesArray.add(row);
        }
        levelJson.add("tiles", tilesArray);

        JsonArray objectsArray = getJsonElements();
        levelJson.add("objects", objectsArray);

        return levelJson;
    }

    /**
     * Generates a JSON array containing the game objects in the level.
     *
     * @return the JSON array containing the game objects
     */
    private JsonArray getJsonElements() {
        JsonArray objectsArray = new JsonArray();
        for (GameObject object : objects) {
            JsonObject objectJson = new JsonObject();
            if (object instanceof Chest) {
                objectJson.addProperty("type", "chest");
            } else if (object instanceof Key) {
                objectJson.addProperty("type", "key");
            } else if (object instanceof Coin) {
                objectJson.addProperty("type", "coin");
            } else if (object instanceof Door) {
                objectJson.addProperty("type", "door");
            } else if (object instanceof NPC) {
                objectJson.addProperty("type", "npc");
            } else if (object instanceof Potion) {
                objectJson.addProperty("type", "potion");
            } else if (object instanceof Heal) {
                objectJson.addProperty("type", "heal");
            } else if (object instanceof QuestKey) {
                objectJson.addProperty("type", "questKey");
            } else if (object instanceof LevelDoor) {
                objectJson.addProperty("type", "levelDoor");
            }

            objectJson.addProperty("x", object.getX());
            objectJson.addProperty("y", object.getY());
            objectsArray.add(objectJson);
        }
        return objectsArray;
    }

    /**
     * Retrieves the height of the level.
     *
     * @return the height of the level in tiles
     */
    public int getHeight() {
        return tiles[0].length;
    }


    // for level doors x - 24 y - 46
}
