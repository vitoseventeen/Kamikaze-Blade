package ModelTest;

import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {

    private Level level;

    @BeforeEach
    void setUp() {
        // Create a sample level for testing
        Tile[][] tiles = {
                {new Tile(SurfaceType.FLOOR), new Tile(SurfaceType.FLOOR), new Tile(SurfaceType.FLOOR)},
                {new Tile(SurfaceType.FLOOR), new Tile(SurfaceType.WALL), new Tile(SurfaceType.FLOOR)},
                {new Tile(SurfaceType.FLOOR), new Tile(SurfaceType.FLOOR), new Tile(SurfaceType.FLOOR)}
        };
        List<GameObject> objects = List.of(
                new Chest(1, 1),
                new Key(2, 2),
                new Coin(3, 3)
                // Add more objects for thorough testing
        );
        level = new Level(tiles, objects);
    }

    @Test
    void testGetWidth() {
        assertEquals(3, level.getWidth());
    }

    @Test
    void testGetHeight() {
        assertEquals(3, level.getHeight());
    }

    @Test
    void testGetTile() {
        assertEquals(SurfaceType.FLOOR, level.getTile(0, 0).getSurfaceType());
        assertEquals(SurfaceType.WALL, level.getTile(1, 1).getSurfaceType());
        assertEquals(SurfaceType.EMPTY, level.getTile(-1, -1).getSurfaceType());
    }

    @Test
    void testGetObjects() {
        assertEquals(3, level.getObjects().size());
    }


    @Test
    void testGetLevelJson() {
        // Test if the generated JSON object is not null
        assertNotNull(level.getLevelJson());
        // Add more assertions based on the expected contents of the generated JSON object
    }
}
