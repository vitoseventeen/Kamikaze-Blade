package Core;

import Core.Controller.GameManager;
import Core.View.Menu;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        new Menu(gameManager);
    }
}


