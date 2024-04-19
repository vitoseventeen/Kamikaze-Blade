package Core.Controller;

import Core.Model.Level;
import Core.Model.LevelFactory;
import Core.Model.LevelManager;
import Core.Model.Player;
import Core.View.Panel;
import Core.View.View;

import static Core.Util.Constants.TARGET_FPS;

public class GameManager implements Runnable {
    private volatile boolean running = false;
    private Thread gameThread;
    private final Player player;
    private final View view;
    private Level level;
    private InputHandler inputHandler;
    private final Controller controller;
    private LevelManager levelManager;

    public GameManager() {
        this.level = LevelFactory.createLevel();
        player = new Player("Ninja", 100, 100, 32, 32);
        view = new View(player);
        Panel panel = view.getPanel();
        levelManager = new LevelManager();
        controller = new Controller(player, panel, levelManager, level);
        inputHandler = new InputHandler(controller);
    }

    public void start() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();

            new GameUpdateThread(this).start();
            new GameRenderThread(this).start();
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
        double nsPerTick = 1000000000.0 / TARGET_FPS;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            render();
        }
    }

    void update() {
    }

    void render() {
        view.getPanel().repaint();
    }
}
