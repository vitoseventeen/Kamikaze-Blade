package ControllerTest;

import Controller.GameManager;
import Model.*;
import Util.Constants;
import View.GamePanel;
import Controller.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ControllerTest {

    private Player player;
    private GamePanel gamePanel;
    private Level level;
    private List<Enemy> enemies;
    private GameManager gameManager;
    private Controller controller;

    @BeforeEach
    public void setUp() {
        Inventory playerInventory = new Inventory(14);
        player = new Player("Ninja", 640, 512, Constants.PLAYER_HEIGHT, Constants.PLAYER_WIDTH, playerInventory);

        // Mocking Level
        level = mock(Level.class);
        when(level.getWidth()).thenReturn(30);
        when(level.getHeight()).thenReturn(30);

        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                when(level.getTile(x, y)).thenReturn(new Tile(SurfaceType.FLOOR));
            }
        }

        gamePanel = mock(GamePanel.class);
        List<GameObject> gameObjects = new ArrayList<>();
        when(gamePanel.getObjects()).thenReturn(gameObjects.toArray(new GameObject[0]));

        enemies = new ArrayList<>();
        gameManager = mock(GameManager.class);
        controller = new Controller(player, gamePanel, level, enemies, gameManager);
    }


    @Test
    public void testUpdatePlayerMovement() {
        // Test moving right
        controller.updatePlayerMovement(1, 0);
        assertEquals(641, player.getX());
        assertEquals(512, player.getY());

        // Test moving down
        controller.updatePlayerMovement(0, 1);
        assertEquals(641, player.getX());
        assertEquals(513, player.getY());

        // Test moving left
        controller.updatePlayerMovement(-1, 0);
        assertEquals(640, player.getX());
        assertEquals(513, player.getY());

        // Test moving up
        controller.updatePlayerMovement(0, -1);
        assertEquals(640, player.getX());
        assertEquals(512, player.getY());
    }

    @Test
    public void testSpawnEnemies() {
        controller.spawnEnemies();
        assertEquals(Constants.NUMBER_OF_ENEMIES, enemies.size());
        for (Enemy enemy : enemies) {
            assertFalse(controller.isCollision(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight()));
        }
    }

    @Test
    public void testCraftHeal() {
        Potion potion1 = new Potion(0,0);
        Potion potion2 = new Potion(0,0);
        player.getInventory().addItem(potion1);
        player.getInventory().addItem(potion2);
        controller.craftHeal();
        assertEquals(1, player.getInventory().getItems().size());
        assertEquals(GameObjectType.HEAL, player.getInventory().getItems().get(0).getType());
    }

    @Test
    public void testAttack() {
        Enemy enemy = new Enemy("enemy",640, 512, 16, 16);
        enemies.add(enemy);
        for (int i = 0; i < 4; i++) {
            controller.attack();
        }
        assertTrue(enemy.isDead());
    }
}
