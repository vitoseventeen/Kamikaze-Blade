package Core.Controller;

import Core.Model.Player;
import Core.View.Panel;
import Core.View.View;

public class GameManager implements Runnable {
    private static final int TARGET_FPS = 60;
    private boolean running = false;
    private final Player player;
    private final View view;
    private final Controller controller;
    private final InputHandler inputHandler;
    private Thread gameThread;

    public GameManager() {
        player = new Player("Ninja", 100, 100, 32, 32);
        view = new View(player);
        Panel panel = view.getPanel();
        controller = new Controller(player, panel);
        inputHandler = new InputHandler(controller);
    }

    public void start() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
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

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / TARGET_FPS;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = false;

            while (delta >= 1) {
                update();
                delta--;
                shouldRender = true;
            }

            if (shouldRender) {
                render();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
    }

    private void update() {
        // check collision
//        for (Enemy enemy : enemies) {
//            if (player.checkCollisionWithEnemy(enemy)) {
//                player.takeDamage(enemy.getDamage());
//            }
//        }
    }

    private void render() {
        view.getPanel().repaint();
    }
}
