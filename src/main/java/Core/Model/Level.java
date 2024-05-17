package Core.Model;

import Core.Model.SurfaceType;
import Core.Model.Tile;
import Core.View.GamePanel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Level {
    public static final List<Level> levels = new ArrayList<>();
    private final Tile[][] tiles;
    private final List<GameObject> objects; // Добавим список объектов


    public Level(Tile[][] tiles, List<GameObject> objects) {
        this.tiles = tiles;
        this.objects = objects;
    }

    public static Level loadLevelFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            JsonObject levelJson = gson.fromJson(reader, JsonObject.class);

            int width = levelJson.get("width").getAsInt();
            int height = levelJson.get("height").getAsInt();
            JsonArray tilesArray = levelJson.getAsJsonArray("tiles");
            JsonArray objectsArray = levelJson.getAsJsonArray("objects"); // Загружаем массив объектов

            Tile[][] tiles = new Tile[width][height];
            List<GameObject> objects = new ArrayList<>(); // Создаем список объектов

            // Загружаем тайлы
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

            // Загружаем объекты
            GamePanel.loadObjects(objectsArray, objects);

            Level level = new Level(tiles, objects);
            levels.add(level);
            return level;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Добавим геттер для объектов
    public List<GameObject> getObjects() {
        return objects;
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


    public int getHeight() {
        return tiles[0].length;
    }

    public void clearTiles() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                tiles[x][y] = new Tile(SurfaceType.EMPTY);
            }
        }
    }

    // for level doors x - 24 y - 46



}
