package Core.Controller;

import Core.Model.Level;
import Core.Model.Player;
import Core.Util.Constants;
import Core.View.Panel;
import Core.View.View;
public class GameManager implements Runnable {
    private volatile boolean running = false;
    private Thread gameThread;
    private final Player player;
    private View view;
    private Level level;
    private InputHandler inputHandler;
    private final Controller controller;

    public GameManager() {
        player = new Player("Ninja", 100, 100, 32, 32);
        level = Level.loadLevelFromJson("level.json");
        view = new View(player, level);
        Panel panel = view.getPanel();
        controller = new Controller(player, panel, level);
        inputHandler = new InputHandler(controller);

    }




    public void start() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();

            new Thread(() -> {
                while (running) {
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
                updateGame();
                delta--;
            }
        }
        stop();
    }

    private void updateGame() {
        view.getPanel().repaint();
    }
}
