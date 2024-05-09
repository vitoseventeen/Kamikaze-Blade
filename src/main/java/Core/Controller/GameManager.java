package Core.Controller;

import Core.Model.Enemy;
import Core.Model.Level;
import Core.Model.Player;
import Core.Util.Constants;
import Core.View.View;

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
    private final Controller controller;

    public GameManager() {
        player = new Player("Ninja", 640, 512, Constants.PLAYER_HEIGHT, Constants.PLAYER_WIDTH);
        level = Level.loadLevelFromJson("level.json");
        enemies = new ArrayList<>();
        this.view = new View(player, level, enemies);
        controller = new Controller(player, view.getPanel(), level, enemies);
        inputHandler = new InputHandler(controller);
    }




    public void start() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();

            // zoom game
            view.getPanel().setZoomFactor(ZOOM_FACTOR);

            spawnEnemies();

            view.getPanel().setEnemies(enemies);

            new Thread(() -> {
                while (running) {
                    controller.moveEnemies();
                    view.getPanel().repaint();
                    try {
                        Thread.sleep(1000 / Constants.TARGET_FPS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
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
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / Constants.TARGET_FPS;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (delta >= 1) {
                controller.moveEnemies();

                delta--;
            }
        }
        stop();
    }


}
