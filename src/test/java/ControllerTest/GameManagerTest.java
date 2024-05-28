package ControllerTest;

import Controller.Controller;
import Controller.GameManager;
import Model.Enemy;
import Model.Inventory;
import Model.Level;
import Model.Player;
import Util.Constants;
import View.DeathMenu;
import View.GamePanel;
import View.View;
import View.WinMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameManagerTest {

    private GameManager gameManager;
    private Player player;
    private List<Enemy> enemies;
    private View view;
    private Level level;
    private Controller controller;
    private GamePanel panel;
    private JFrame frame;
    private JLayeredPane layeredPane;

    @BeforeEach
    void setUp() {
        Inventory playerInventory = new Inventory(14);
        player = new Player("Ninja", 640, 512, Constants.PLAYER_HEIGHT, Constants.PLAYER_WIDTH, playerInventory);
        level = Level.loadLevelFromJson("level1.json");
        enemies = new ArrayList<>();
        gameManager = spy(new GameManager());

        view = mock(View.class);
        controller = mock(Controller.class);
        panel = mock(GamePanel.class); // Mock the actual type
        frame = mock(JFrame.class);
        layeredPane = mock(JLayeredPane.class);

        gameManager.view = view;
        gameManager.controller = controller;

        when(view.getPanel()).thenReturn(panel); // Return the correct mock type
        when(view.getFrame()).thenReturn(frame);
        when(frame.getLayeredPane()).thenReturn(layeredPane);
    }

    @Test
    void testStart() {
        gameManager.start();
        assertTrue(gameManager.running);
    }

    @Test
    void testTogglePause() {
        gameManager.start();
        assertFalse(gameManager.isPaused());
        gameManager.togglePause();
        assertTrue(gameManager.isPaused());
        gameManager.togglePause();
        assertFalse(gameManager.isPaused());
    }


    @Test
    void testShowInventoryMenu() {
        gameManager.start();
        gameManager.showInventoryMenu();
        assertTrue(gameManager.showingInventory());
        gameManager.showInventoryMenu();
        assertFalse(gameManager.showingInventory());
    }

    @Test
    void testHideInventoryMenu() {
        gameManager.start();
        gameManager.showInventoryMenu();
        gameManager.hideInventoryMenu();
        assertFalse(gameManager.showingInventory());
    }

    @Test
    void testShowDeathMenu() {
        gameManager.start();
        player.setHealth(0);
        gameManager.showDeathMenu();
        assertTrue(gameManager.showingDeathMenu());
    }

    @Test
    void testShowWinMenu() {
        gameManager.start();
        gameManager.showWinMenu();
        assertTrue(gameManager.showingWinMenu());
    }

    @Test
    void loadInventory() {
        gameManager.loadInventory();
        assertNotNull(player.getInventory());
    }

    @Test
    void saveInventory() {
        gameManager.saveInventory();
        assertNotNull(player.getInventory());
    }

}
