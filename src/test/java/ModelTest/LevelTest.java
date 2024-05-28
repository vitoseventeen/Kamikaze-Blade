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




}
