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

import static Util.Constants.ATTACK_RADIUS;
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
        Potion potion1 = new Potion(0, 0);
        Potion potion2 = new Potion(0, 0);
        player.getInventory().addItem(potion1);
        player.getInventory().addItem(potion2);
        controller.craftHeal();
        assertEquals(1, player.getInventory().getItems().size());
        assertEquals(GameObjectType.HEAL, player.getInventory().getItems().get(0).getType());
    }

    @Test
    public void testAttack() {
        Enemy enemy = new Enemy("enemy", 640, 512, 16, 16);
        enemies.add(enemy);
        for (int i = 0; i < 4; i++) {
            controller.attack();
        }
        assertTrue(enemy.isDead());
    }


    @Test
    public void testInteractWithChest() {
        Chest chest = new Chest(640, 512);
        List<GameObject> objects = new ArrayList<>();
        objects.add(chest);
        when(gamePanel.getObjects()).thenReturn(objects.toArray(new GameObject[0]));
        controller.interact();
        assertTrue(chest.isOpened());
    }


    @Test
    public void testOpenDoorWithoutFalse() {
        Door door = new Door(640, 512);
        List<GameObject> objects = new ArrayList<>();
        objects.add(door);
        when(gamePanel.getObjects()).thenReturn(objects.toArray(new GameObject[0]));
        controller.interact();
        assertFalse(door.isOpened());
    }

    @Test
    public void testPlayerPickUpCoin() {
        Coin coin = new Coin(640, 512);
        List<GameObject> objects = new ArrayList<>();
        objects.add(coin);
        when(gamePanel.getObjects()).thenReturn(objects.toArray(new GameObject[0]));
        controller.interact();
        assertEquals(1, player.getInventory().getCoinBalance());
    }

    @Test
    public void testPlayerPickUpKey() {
        Key key = new Key(640, 512);
        List<GameObject> objects = new ArrayList<>();
        objects.add(key);
        when(gamePanel.getObjects()).thenReturn(objects.toArray(new GameObject[0]));
        controller.interact();
        assertEquals(1, player.getInventory().getItems().size());
        assertEquals(GameObjectType.KEY, player.getInventory().getItems().get(0).getType());
    }

    @Test
    public void testPlayerPickUpPotion() {
        Potion potion = new Potion(640, 512);
        List<GameObject> objects = new ArrayList<>();
        objects.add(potion);
        when(gamePanel.getObjects()).thenReturn(objects.toArray(new GameObject[0]));
        controller.interact();
        assertEquals(1, player.getInventory().getItems().size());
        assertEquals(GameObjectType.POTION, player.getInventory().getItems().get(0).getType());
    }

    @Test
    public void testPlayerPickUpHeal() {
        Heal heal = new Heal(640, 512);
        List<GameObject> objects = new ArrayList<>();
        objects.add(heal);
        when(gamePanel.getObjects()).thenReturn(objects.toArray(new GameObject[0]));
        controller.interact();
        assertEquals(1, player.getInventory().getItems().size());
        assertEquals(GameObjectType.HEAL, player.getInventory().getItems().get(0).getType());
    }

    @Test
    public void testLevelTransition() {
        when(level.getTile(anyInt(), anyInt())).thenReturn(new Tile(SurfaceType.LEVELTILE));
        controller.updatePlayerMovement(1, 0);
        verify(level, times(1)).getTile(anyInt(), anyInt());
    }

    @Test
    public void testPlayerDeath() {
        player.takeDamage(player.getHealth());
        assertTrue(player.isDead());
    }

    @Test
    public void testOpenDoorWithoutKey() {
        Door door = new Door(640, 512);
        List<GameObject> objects = new ArrayList<>();
        objects.add(door);
        when(gamePanel.getObjects()).thenReturn(objects.toArray(new GameObject[0]));
        controller.interact();
        assertFalse(door.isOpened());
    }



    @Test
    public void testLoadNextLevel() {
        // Simulate player reaching the next level
        player.setX(0);
        player.setY(0);

        when(level.getTile(anyInt(), anyInt())).thenReturn(new Tile(SurfaceType.LEVELTILE));

        controller.updatePlayerMovement(-1, 0);

        verify(gamePanel, times(1)).clearObjects();
        verify(gamePanel, times(1)).setLevel(any(Level.class));
        assertTrue(controller.isLevelChanged());
    }

    @Test
    public void testTogglePause() {
        when(gameManager.isPaused()).thenReturn(false);
        controller.togglePause();
        verify(gameManager, times(1)).togglePause();
    }

    @Test
    public void testMoveEnemyRandomly() {
        Enemy enemy = new Enemy("enemy", 640, 512, 16, 16);
        enemies.add(enemy);

        for (int i = 0; i < 10; i++) {
            controller.moveEnemy(enemy);
        }

        assertTrue(enemy.getX() >= 0 && enemy.getX() < level.getWidth() * Constants.TILE_SIZE);
        assertTrue(enemy.getY() >= 0 && enemy.getY() < level.getHeight() * Constants.TILE_SIZE);
    }

    @Test
    public void testShowHideInventory() {
        controller.showInventory();
        verify(gameManager, times(1)).showInventoryMenu();

        controller.hideInventory();
        verify(gameManager, times(1)).hideInventoryMenu();
    }


    @Test
    public void testNearbyEnemy() {
        Enemy enemy = new Enemy("enemy", 640, 512, 16, 16);
        enemies.add(enemy);

        assertTrue(controller.isNearbyEnemy(player.getX(), player.getY(), enemy.getX(), enemy.getY()));
    }

    @Test
    public void testNearbyEnemyFalse() {
        Enemy enemy = new Enemy("enemy", 640, 512, 16, 16);
        enemies.add(enemy);

        assertFalse(controller.isNearbyEnemy(player.getX() + 100, player.getY() + 100, enemy.getX(), enemy.getY()));
    }

}
