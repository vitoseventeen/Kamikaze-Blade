package Core.Controller;

public class GameUpdateThread extends Thread {
    private GameManager gameManager;

    public GameUpdateThread(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        while (gameManager.isRunning()) {
            gameManager.update();
        }
    }
}
