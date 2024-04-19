package Core.Controller;

import Core.Model.Player;
import Core.View.Panel;

public class Controller {
    private Player player;
    private Panel panel;

    public Controller(Player player, Panel panel) {
        this.player = player;
        this.panel = panel;
    }

    public void moveLeft() {
        player.setX(player.getX() - player.getSpeed());
        panel.repaint();
    }

    public void moveRight() {
        player.setX(player.getX() + player.getSpeed());
        panel.repaint();
    }

    public void moveUp() {
        player.setY(player.getY() - player.getSpeed());
        panel.repaint();
    }

    public void moveDown() {
        player.setY(player.getY() + player.getSpeed());
        panel.repaint();
    }
}
