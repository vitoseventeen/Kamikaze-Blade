package Core;

import Core.Controller.GameManager;
import Core.View.Menu;

/**
 * Main class for the game
 */

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        new Menu(gameManager);
    }
}
