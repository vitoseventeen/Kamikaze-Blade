package Core.Controller;

import Core.Model.Player;
import Core.View.View;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Ninja", 100, 100);
        View view = new View(player);
        InputController inputController = new InputController(player, view);

    }
}
