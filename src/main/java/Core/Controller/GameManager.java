package Core.Controller;

import Core.Model.Enemy;
import Core.Model.Inventory;
import Core.Model.Level;
import Core.Model.Player;
import Core.Util.Constants;
import Core.View.DeathMenu;
import Core.View.InventoryMenu;
import Core.View.PauseMenu;
import Core.View.View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Core.Util.Constants.*;

public class GameManager implements Runnable {
    private volatile boolean running = false;
    private Thread gameThread;
    private final Player player;
    private final List<Enemy> enemies;
    private View view;
    private Level level;
    private InputHandler inputHandler;
    private Controller controller;
    private PauseMenu pauseMenu;
    private DeathMenu deathMenu;
    private InventoryMenu inventoryMenu;
    private boolean isInMenu = false;
    private boolean paused = false;

    public GameManager() {
        Inventory playerInventory = new Inventory(10);
        player = new Player("Ninja", 640, 512, Constants.PLAYER_HEIGHT, Constants.PLAYER_WIDTH, playerInventory);
        level = Level.loadLevelFromJson("level1.json");
        enemies = new ArrayList<>();

    }

    public void start() {
        this.view = new View(player, level, enemies, this);
        controller = new Controller(player, view.getPanel(), level, enemies, this);
        inputHandler = new InputHandler(controller);
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();

            // zoom game
            view.getPanel().setZoomFactor(ZOOM_FACTOR);

            spawnEnemies();

            view.getPanel().setEnemies(enemies);
        }
    }

    private void spawnEnemies() {
        Random random = new Random();
        for (int i = 0; i < Constants.NUMBER_OF_ENEMIES; i++) {
            int x, y;
            do {
                x = random.nextInt(level.getWidth() * Constants.TILE_SIZE);
                y = random.nextInt(level.getHeight() * Constants.TILE_SIZE);
            } while (controller.isCollision(x, y, PLAYER_WIDTH, PLAYER_HEIGHT));
            Enemy enemy = new Enemy("Enemy" + i, x, y, PLAYER_HEIGHT, PLAYER_WIDTH);
            enemies.add(enemy);
        }
    }

    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    private void updateGame() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / Constants.TARGET_FPS;
        double delta = 0;

        while (running) {
            if (player.isDead()) {
                showDeathMenu();
            }
            else {

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

    public void saveInventory() {
        System.out.println("Saving inventory");

    }

    public void loadInventory() {
        System.out.println("Loading inventory");

    }


    public void togglePause() {
        if (!isInMenu) {
            if (pauseMenu == null) {

                pauseMenu = new PauseMenu(this);
                pauseMenu.setOpaque(false);
                pauseMenu.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
                view.getFrame().getLayeredPane().add(pauseMenu, JLayeredPane.POPUP_LAYER);

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

    public void showDeathMenu() {
        if (deathMenu == null) {
            deathMenu = new DeathMenu(this);
            deathMenu.setOpaque(false);
            deathMenu.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
            view.getFrame().getLayeredPane().add(deathMenu, JLayeredPane.POPUP_LAYER);
        }
        deathMenu.setVisible(true);
        paused = true;
        view.getFrame().setCursor(Cursor.getDefaultCursor());
    }

    public boolean isPaused() {
        return paused;
    }

    public void showInventoryMenu() {
        if (inventoryMenu == null) {
            inventoryMenu = new InventoryMenu(this, player);
            inventoryMenu.setOpaque(false);
            inventoryMenu.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
            view.getFrame().getLayeredPane().add(inventoryMenu, JLayeredPane.POPUP_LAYER);

            inventoryMenu.setVisible(true);
            isInMenu = true;
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

    public boolean showingInventory() {
        return inventoryMenu != null && inventoryMenu.isVisible();
    }

    public void hideInventoryMenu() {
        if (inventoryMenu != null) {
            inventoryMenu.setVisible(false);
            isInMenu = false;
            paused = false;
            view.getFrame().setCursor(view.getFrame().getToolkit().createCustomCursor(
                    new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
        }
    }
}
