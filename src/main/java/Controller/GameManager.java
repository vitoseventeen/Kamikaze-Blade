package Controller;

import Model.Enemy;
import Model.Inventory;
import Model.Level;
import Model.Player;
import Util.Constants;
import View.DeathMenu;
import View.PauseMenu;
import View.InventoryMenu;
import View.View;
import View.WinMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import static Util.Constants.*;


/* GameManager class is responsible for managing the game state, including the player, enemies, level, and menus. */

public class GameManager implements Runnable {
    public volatile boolean running = false;
    private Thread gameThread;
    private final Player player;
    private final List<Enemy> enemies;
    public View view;
    private final Level level;
    public Controller controller;
    public DeathMenu deathMenu;
    public WinMenu winMenu;
    private InventoryMenu inventoryMenu;
    private PauseMenu pauseMenu;
    private boolean isInMenu = false;
    private boolean isShowingDeathMenu = false;
    private boolean isShowingWinMenu = false;
    private boolean paused = false;
    private static final Logger logger = Logger.getLogger(GameManager.class.getName());

    /**
     * Initializes the game manager with a player, level, and empty enemy list.
     */
    public GameManager() {
        Inventory playerInventory = new Inventory(14);
        player = new Player("Ninja", 640, 512, Constants.PLAYER_HEIGHT, Constants.PLAYER_WIDTH, playerInventory);
        level = Level.loadLevelFromJson("level1.json");
        enemies = new ArrayList<>();
    }

    /**
     * Starts the game loop, initializes the view and controller.
     */
    public void start() {
        this.view = new View(player, level, enemies, this);
        controller = new Controller(player, view.getPanel(), level, enemies, this);
        new InputHandler(controller);
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
            view.getPanel().setZoomFactor(ZOOM_FACTOR);
            controller.spawnEnemies();
            view.getPanel().setEnemies(enemies);
        }
        logger.info("Game started.");
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException _) {
            }
        }
        logger.info("Game stopped.");
    }


    /**
     * Runs the game loop.
     */


    @Override
    public void run() {
        while (running) {
            for (Enemy enemy : enemies) {
                if (!enemy.isDead()) {
                    if (controller.isPlayerInEnemyRadius(enemy, ATTACK_RADIUS)) {
                        enemy.setAnimationType(Enemy.AnimationType.ATTACK);
                        player.takeDamage(1);
                    } else {
                        enemy.setAnimationType(Enemy.AnimationType.IDLE);
                    }
                }
            }
            if (!paused) {
                updateGame();
                view.getPanel().repaint();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException _) {
            }
        }
        stop();
    }

    /**
     * Updates the game state.
     */
    private void updateGame() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / Constants.TARGET_FPS;
        double delta = 0;

        while (running) {
            if (player.isDead()) {
                showDeathMenu();
            } else {
                long now = System.nanoTime();
                delta += (now - lastTime) / nsPerTick;
                lastTime = now;
                while (delta >= 1 && !paused) {
                    controller.moveEnemies();
                    delta--;
                }
            }
        }
    }

    /**
     * Saves the player's inventory to a file.
     */
    public void saveInventory() {
        player.getInventory().saveInventory("inventory.dat");
        logger.info("Player inventory saved.");
    }

    /**
     * Loads the player's inventory from a file.
     */
    public void loadInventory() {
        Inventory loadedInventory = Inventory.loadInventory("inventory.dat");
        if (loadedInventory != null) {
            player.setInventory(loadedInventory);
            start();
            logger.info("Player inventory loaded.");
        } else {
            logger.warning("Failed to load player inventory.");
        }
    }

    /**
     * Toggles the game pause state.
     */
    public void togglePause() {
        if (!isInMenu) {
            if (pauseMenu == null) {
                pauseMenu = new PauseMenu(this);
                pauseMenu.setOpaque(false);
                pauseMenu.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
                view.getFrame().getLayeredPane().add(pauseMenu, JLayeredPane.POPUP_LAYER);
                logger.info("Pause menu created.");
            }
            paused = !paused;
            pauseMenu.setVisible(paused);
            if (paused) {
                view.getFrame().setCursor(Cursor.getDefaultCursor());
            } else {
                view.getFrame().setCursor(view.getFrame().getToolkit().createCustomCursor(
                        new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
            }
        }
    }

    /**
     * Displays the death menu when the player dies.
     */
    public void showDeathMenu() {
        if (deathMenu == null) {
            deathMenu = new DeathMenu(this);
            deathMenu.setOpaque(false);
            deathMenu.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
            view.getFrame().getLayeredPane().add(deathMenu, JLayeredPane.POPUP_LAYER);
            logger.info("Death menu created.");
            isShowingDeathMenu = true;
        }
        deathMenu.setVisible(true);
        paused = true;
        view.getFrame().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Checks if the game is paused.
     *
     * @return true if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Displays or hides the inventory menu.
     */
    public void showInventoryMenu() {
        if (player.isDead()) {
            hideInventoryMenu();
            return;
        }
        if (inventoryMenu == null) {
            inventoryMenu = new InventoryMenu(this, player);
            inventoryMenu.setOpaque(false);
            inventoryMenu.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
            view.getFrame().getLayeredPane().add(inventoryMenu, JLayeredPane.POPUP_LAYER);
            inventoryMenu.setVisible(true);
            isInMenu = true;
            logger.info("Inventory menu created.");
        } else {
            inventoryMenu.setVisible(!inventoryMenu.isVisible());
            isInMenu = !isInMenu;
        }
        paused = !paused;
        if (paused) {
            view.getFrame().setCursor(Cursor.getDefaultCursor());
        } else {
            view.getFrame().setCursor(view.getFrame().getToolkit().createCustomCursor(
                    new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
        }
    }

    /**
     * Checks if the inventory menu is currently displayed.
     *
     * @return true if the inventory menu is visible, false otherwise.
     */
    public boolean showingInventory() {
        return inventoryMenu != null && inventoryMenu.isVisible();
    }

    /**
     * Hides the inventory menu.
     */
    public void hideInventoryMenu() {
        if (inventoryMenu != null) {
            inventoryMenu.setVisible(false);
            isInMenu = false;
            paused = false;
            view.getFrame().setCursor(view.getFrame().getToolkit().createCustomCursor(
                    new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
            logger.info("Inventory menu hidden.");
        }
    }

    /**
     * Displays the win menu when the player wins the game. ( find a book in mysterious chest )
     */

    public void showWinMenu() {
        if (winMenu == null) {
            winMenu = new WinMenu(this);
            winMenu.setOpaque(false);
            winMenu.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
            view.getFrame().getLayeredPane().add(winMenu, JLayeredPane.POPUP_LAYER);
            logger.info("Win menu created.");
            isShowingWinMenu = true;
        }
        winMenu.setVisible(true);
        paused = true;
        view.getFrame().setCursor(Cursor.getDefaultCursor());
    }

    public boolean showingDeathMenu() {
        return isShowingDeathMenu;
    }

    public boolean showingWinMenu() {
        return isShowingWinMenu;
    }
}
