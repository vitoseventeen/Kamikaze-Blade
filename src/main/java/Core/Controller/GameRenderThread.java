package Core.Controller;

public class GameRenderThread extends Thread {
    private GameManager gameManager;

    public GameRenderThread(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        while (gameManager.isRunning()) {
            gameManager.render();
        }
    }
}
