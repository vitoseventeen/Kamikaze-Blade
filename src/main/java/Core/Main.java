package Core;

import Core.Controller.InputController;
import Core.Model.Player;
import Core.View.View;

public class Main implements Runnable {
    private static final int TARGET_FPS = 60;
    private boolean running = false;
    private final Player player;
    private final View view;


    public Main() {
        player = new Player("Ninja", 100, 100);
        view = new View(player);
        InputController inputController = new InputController(player, view);
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
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
        // Обновление игры
        // player.update();
    }

    private void render() {
        // Отрисовка игры
        view.getPanel().repaint();
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.start();
    }
}
